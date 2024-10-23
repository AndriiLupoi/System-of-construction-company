package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SiteService {

    @Autowired
    private SiteRepository siteRepository;

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    // Логіка пошуку сайтів
    public List<Site> searchSites(String query) {
        List<Site> results = new ArrayList<>();

        // Спробуємо спочатку розпізнати як число (ID управління)
        try {
            Integer id = Integer.parseInt(query);
            results.addAll(siteRepository.findByNameContainingOrManagementIdOrLocationContaining(null, id, null));
        } catch (NumberFormatException e) {
            // Якщо це не число, шукаємо по назві і локації
            results.addAll(siteRepository.findByNameContainingOrManagementIdOrLocationContaining(query, null, query));
        }

        return results;
    }


}
