package com.ms.companyms.company;

import java.util.List;

import com.ms.companyms.company.dto.ReviewMessage;

public interface CompanyService {
    List<Company> getAllCompanies();
    void createCompany(Company company);
    Company getCompanyById(Long id);
    boolean updatedCompany(Long id, Company company);
    boolean deleteCompany(Long id);
    void updateCompanyRating(ReviewMessage reviewMessage);
}
