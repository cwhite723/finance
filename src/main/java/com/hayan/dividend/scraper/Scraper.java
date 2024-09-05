package com.hayan.dividend.scraper;

import com.hayan.dividend.domain.Company;
import com.hayan.dividend.domain.Dividend;

import java.util.List;

public interface Scraper {
    Company scrapCompany(String ticker);
    List<Dividend> scrapDividends(Company company);
}
