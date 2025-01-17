package com.ms.jobms.job.impl;

import com.ms.jobms.job.Job;
import com.ms.jobms.job.JobRepository;
import com.ms.jobms.job.JobService;
import com.ms.jobms.job.clients.CompanyClient;
import com.ms.jobms.job.clients.ReviewClient;
import com.ms.jobms.job.dto.JobDTO;
import com.ms.jobms.job.external.Company;
import com.ms.jobms.job.external.Review;
import com.ms.jobms.job.mapper.JobMapper;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, 
        ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    // @Retry(name = "companyBreaker", 
    //     fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name = "companyBreaker")
    public List<JobDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        // List<JobDTO> jobDTOs = new ArrayList<>();

        return jobs.stream().map(this::coverTDto)
            .collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e) {
        List<String> list = new ArrayList<>();
        list.add("dummy");
        return list;
    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job = jobRepository.findById(id).orElse(null);
        return coverTDto(job);
    }

    @Override
    public boolean deleteJobById(Long id) {
        try {
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return  true;
        }
        return false;
    }

    private JobDTO coverTDto(Job job) {
        Company company = companyClient.getCompany(job.getCompanyId());
        
        List<Review> reviews = reviewClient.getReview(job.getCompanyId());

        JobDTO jobDTO = JobMapper
            .mapToJobWithCompanyDTO(job, company, reviews);
        // jobWithCompanyDTO.setCompany(company);

        return jobDTO;
    }
}
