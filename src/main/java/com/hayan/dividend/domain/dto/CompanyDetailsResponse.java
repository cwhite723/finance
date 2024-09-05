package com.hayan.dividend.domain.dto;

import java.util.List;

public record CompanyDetailsResponse(CompanyResponse company, List<DividendResponse> dividends) {
}
