package com.hayan.dividend.repository;

import com.hayan.dividend.domain.Dividend;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DividendBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<Dividend> dividendList) {
        String sql = "INSERT INTO dividends (company_id, date, dividend) VALUES (?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                dividendList,
                dividendList.size(),
                (PreparedStatement ps, Dividend dividend) -> {
                    ps.setLong(1, dividend.getCompany().getId());
                    ps.setDate(2, Date.valueOf(dividend.getDate()));
                    ps.setString(3, dividend.getDividend());
                });
    }

    @Transactional
    public void deleteAllByCompanyId(Long companyId) {
        String sql = "DELETE FROM dividends WHERE company_id = ?";
        jdbcTemplate.update(sql, companyId);
    }
}
