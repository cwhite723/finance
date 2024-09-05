package com.hayan.dividend.controller;

import com.hayan.dividend.domain.dto.CompanyDetailsResponse;
import com.hayan.dividend.domain.dto.CompanyResponse;
import com.hayan.dividend.global.ApplicationResponse;
import com.hayan.dividend.service.FinanceService;
import com.hayan.dividend.service.TrieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FinanceController {

    private final TrieService trieService;
    private final FinanceService financeService;

    @PostMapping("/company")
    public ApplicationResponse<Void> create(@RequestBody String ticker) {

        financeService.save(ticker);

        return ApplicationResponse.noData();
    }

    @GetMapping("/company")
    public ApplicationResponse<Page<CompanyResponse>> getAllCompanies(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {

        var companyList = financeService.loadAllCompanies(page, size);

        return ApplicationResponse.ok(companyList);
    }

    @GetMapping("/finance/dividend/{company-name}")
    public ApplicationResponse<CompanyDetailsResponse> getCompany(@PathVariable("company-name") String name) {

        var companyDetails = financeService.loadCompanyDetails(name);

        return ApplicationResponse.ok(companyDetails);
    }

    @DeleteMapping("/company/{ticker}")
    public ApplicationResponse<Void> delete(@PathVariable("ticker") String ticker) {

        financeService.delete(ticker);

        return ApplicationResponse.noData();
    }

    @GetMapping("/company/autocomplete")
    public ApplicationResponse<List<String>> autocomplete(@RequestParam String prefix) {
        List<String> results = trieService.autocomplete(prefix);

        return ApplicationResponse.ok(results);
    }
}
