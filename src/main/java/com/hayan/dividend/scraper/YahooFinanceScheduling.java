package com.hayan.dividend.scraper;

import com.hayan.dividend.domain.constant.CacheKey;
import com.hayan.dividend.domain.Dividend;
import com.hayan.dividend.repository.CompanyRepository;
import com.hayan.dividend.repository.DividendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class YahooFinanceScheduling {

    private final YahooFinanceScraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true)
    @Scheduled(cron = "0 0 0 * * *")
    public void updateDividends() {

        companyRepository.findAll().forEach(company -> {
            List<Dividend> existingDividends = dividendRepository.findAllByCompany(company);
            List<Dividend> scrapedDividends = yahooFinanceScraper.scrapDividends(company);

            List<Dividend> newDividends = scrapedDividends.stream()
                    .filter(scrapedDividend -> existingDividends.stream()
                            .noneMatch(existingDividend -> existingDividend.getDate().equals(scrapedDividend.getDate())))
                    .collect(Collectors.toList());

            dividendRepository.saveAll(newDividends);
        });
    }
}
