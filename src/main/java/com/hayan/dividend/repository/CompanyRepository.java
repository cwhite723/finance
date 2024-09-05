package com.hayan.dividend.repository;

import com.hayan.dividend.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByTicker(String ticker);
    Optional<Company> findByName(String name);
    Optional<Company> findByTicker(String ticker);
}
