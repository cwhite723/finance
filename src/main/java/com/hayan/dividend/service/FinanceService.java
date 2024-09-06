package com.hayan.dividend.service;

import com.hayan.dividend.domain.dto.CompanyDetailsResponse;
import com.hayan.dividend.domain.dto.CompanyResponse;
import org.springframework.data.domain.Page;

public interface FinanceService {
    void save(String ticker);
    Page<CompanyResponse> loadAllCompanies(int page, int size);
    CompanyDetailsResponse loadCompanyDetails(String companyName);
    void delete(String ticker);
}
