package com.hayan.dividend.repository;

import com.hayan.dividend.domain.Company;
import com.hayan.dividend.domain.Dividend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DividendRepository extends JpaRepository<Dividend, Long> {

    List<Dividend> findAllByCompany(Company company);
}
