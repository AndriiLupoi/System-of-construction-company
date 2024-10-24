package com.example.My_Course_Project.service;

import com.example.My_Course_Project.controller.AddController;
import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.repository.SiteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SiteService {
    private static final Logger logger = LoggerFactory.getLogger(AddController.class);

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

    public void saveSite(String name, int managementId, String location) {
        // Створення нового об'єкта сайту
        Site site = new Site();

        // Встановлення значень полів
        site.setName(name);
        site.setManagementId(managementId);
        site.setLocation(location);

        // Збереження об'єкта у базі даних
        siteRepository.save(site);

        // Логування для підтвердження успішного збереження
        logger.info("Site successfully saved: Name = {}, ManagementId = {}, Location = {}", name, managementId, location);
    }


}
