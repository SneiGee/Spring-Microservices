package com.ms.companyms.company.Impl;

import com.ms.companyms.company.Company;
import com.ms.companyms.company.CompanyRepository;
import com.ms.companyms.company.CompanyService;
import com.ms.companyms.company.clients.ReviewClient;
import com.ms.companyms.company.dto.ReviewMessage;

import jakarta.ws.rs.NotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private CompanyRepository companyRepository;
    private ReviewClient reviewClient;


    public CompanyServiceImpl(CompanyRepository companyRepository, ReviewClient reviewClient) {
        this.companyRepository = companyRepository;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public void createCompany(Company company) {
        companyRepository.save(company);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public boolean updatedCompany(Long id, Company company) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isPresent()) {
            Company companyUpdate = companyOptional.get();
            companyUpdate.setName(company.getName());
            companyUpdate.setDescription(company.getDescription());
            companyRepository.save(companyUpdate);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteCompany(Long id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateCompanyRating(ReviewMessage reviewMessage) {
        System.out.print(reviewMessage.getDescription());
        Company company = companyRepository.findById(reviewMessage.getCompanyId())
            .orElseThrow(() -> new NotFoundException("Company not found" + reviewMessage.getCompanyId()));
        
        double averageRating = reviewClient.getAverageRatingForCompany(reviewMessage.getCompanyId());
        company.setRating(averageRating);
        companyRepository.save(company);
    }
}
