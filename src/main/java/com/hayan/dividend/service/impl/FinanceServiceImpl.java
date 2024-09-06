package com.hayan.dividend.service.impl;

import com.hayan.dividend.domain.Company;
import com.hayan.dividend.domain.dto.CompanyDetailsResponse;
import com.hayan.dividend.domain.dto.CompanyResponse;
import com.hayan.dividend.domain.dto.DividendResponse;
import com.hayan.dividend.exception.CustomException;
import com.hayan.dividend.exception.ErrorCode;
import com.hayan.dividend.repository.CompanyRepository;
import com.hayan.dividend.repository.DividendBulkRepository;
import com.hayan.dividend.scraper.Scraper;
import com.hayan.dividend.service.FinanceService;
import com.hayan.dividend.util.CompanyAddedEvent;
import com.hayan.dividend.util.CompanyDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinanceServiceImpl implements FinanceService {

    private final Scraper scraper;
    private final CompanyRepository companyRepository;
    private final DividendBulkRepository dividendBulkRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;

    @Override
    @Transactional
    public void save(String ticker) {
        validateCompanyExist(ticker);
        var company = scraper.scrapCompany(ticker);
        var dividends = scraper.scrapDividends(company);

        companyRepository.save(company);
        dividendBulkRepository.saveAll(dividends);
        eventPublisher.publishEvent(new CompanyAddedEvent(company.getName()));
    }

    @Override
    public Page<CompanyResponse> loadAllCompanies(int page, int size) {
        var companies = companyRepository.findAll(PageRequest.of(page, size));

        return companies.map(company -> new CompanyResponse(
                company.getId(),
                company.getTicker(),
                company.getName()
        ));
    }

    @Override
    @Cacheable(key = "#companyName", value = "finance")
    public CompanyDetailsResponse loadCompanyDetails(String companyName) {
        var company = getCompanyByName(companyName);
        var companyInfo = new CompanyResponse(company.getId(), company.getTicker(), company.getName());
        var dividends = company.getDividends().stream()
                .map(dividend -> new DividendResponse(
                        dividend.getDate(),
                        dividend.getDividend()
                ))
                .toList();

        return new CompanyDetailsResponse(companyInfo, dividends);
    }

    @Override
    @Transactional
    public void delete(String ticker) {
        Company company = getCompanyByTicker(ticker);

        dividendBulkRepository.deleteAllByCompanyId(company.getId());
        companyRepository.delete(company);
        eventPublisher.publishEvent(new CompanyDeletedEvent(company.getName()));

        Cache cache = cacheManager.getCache("finance");
        if (cache != null) {
            cache.evict(company.getName());
        }
    }

    private void validateCompanyExist(String ticker) {
        if (companyRepository.existsByTicker(ticker))
            throw new CustomException(ErrorCode.COMPANY_ALREADY_EXISTS);
    }

    private Company getCompanyByName(String name) {
        return companyRepository.findByName(name)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
    }

    private Company getCompanyByTicker(String ticker) {
        return companyRepository.findByTicker(ticker)
                .orElseThrow(() -> new CustomException(ErrorCode.COMPANY_NOT_FOUND));
    }
}
