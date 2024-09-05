package com.hayan.dividend.scraper;

import com.hayan.dividend.domain.Company;
import com.hayan.dividend.domain.Dividend;
import com.hayan.dividend.domain.constant.Month;
import com.hayan.dividend.exception.CustomException;
import com.hayan.dividend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class YahooFinanceScraper implements Scraper {

    private static final String YAHOO_FINANCE_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=99153000&period2=1725362823&filter=div";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s";

    @Override
    public Company scrapCompany(String ticker) {
        String url = String.format(SUMMARY_URL, ticker);
        Company company = null;

        try {
            Document document = Jsoup.connect(url).get();
            if (document.getElementsByTag("span").text().contains("Symbols similar to")) throw new CustomException(ErrorCode.COMPANY_NOT_FOUND);

            String name = document.getElementsByTag("h1").get(1).text().split("\\(")[0].trim();
            company = new Company(ticker, name);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return company;
    }

    @Override
    public List<Dividend> scrapDividends(Company company) {
        String url = String.format(YAHOO_FINANCE_URL, company.getTicker());
        List<Dividend> dividends = new ArrayList<>();

        try {
            Document document = Jsoup.connect(url).get();
            Elements rows = document.select("tbody tr");

            for (Element row : rows) {
                if (row.text().contains("There are no dividend")) return dividends;

                String dateText = row.select("td").first().text();
                String dividendText = row.select("td.event span").first().text();

                LocalDate date = convertToDate(dateText);
                Dividend dividend = new Dividend(date, dividendText, company);

                dividends.add(dividend);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return dividends;
    }

    private LocalDate convertToDate(String dateText) {
        String[] dateParts = dateText.split(" ");
        String monthPart = dateParts[0];
        int dayPart = Integer.parseInt(dateParts[1].replace(",", ""));
        int yearPart = Integer.parseInt(dateParts[2]);

        int monthNumber = Month.getMonthNumberFromName(monthPart);

        return LocalDate.of(yearPart, monthNumber, dayPart);
    }
}
