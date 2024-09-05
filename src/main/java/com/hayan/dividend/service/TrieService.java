package com.hayan.dividend.service;

import com.hayan.dividend.domain.Company;
import com.hayan.dividend.repository.CompanyRepository;
import com.hayan.dividend.util.CompanyAddedEvent;
import com.hayan.dividend.util.CompanyDeletedEvent;
import com.hayan.dividend.util.Trie;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrieService {

    private final CompanyRepository companyRepository;
    private final Trie trie = new Trie();

    @PostConstruct
    public void init() {
        List<Company> companies = companyRepository.findAll();
        for (Company company : companies) {
            trie.insert(company.getName());
        }
    }

    @EventListener
    public void handleCompanyAddedEvent(CompanyAddedEvent event) {
        trie.insert(event.companyName());
    }

    @EventListener
    public void handleCompanyDeletedEvent(CompanyDeletedEvent event) {
        trie.delete(event.companyName());
    }

    public List<String> autocomplete(String prefix) {
        return trie.autocomplete(prefix);
    }
}