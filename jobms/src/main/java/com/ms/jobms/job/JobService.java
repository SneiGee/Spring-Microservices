package com.ms.jobms.job;

import java.util.List;

import com.ms.jobms.job.dto.JobDTO;

public interface JobService {
    List<JobDTO> findAll();
    void createJob(Job job);
    JobDTO getJobById(Long id);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
