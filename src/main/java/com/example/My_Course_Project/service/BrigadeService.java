package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.repository.BrigadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrigadeService {

    @Autowired
    private BrigadeRepository brigadeRepository;

    public List<Brigade> getAllBrigades() {
        return brigadeRepository.findAll();
    }

    // Логіка пошуку бригад
    public List<Brigade> searchBrigades(String query) {
        try {
            // Якщо query — це числове значення (наприклад, для ID сайту або лідера)
            Integer id = Integer.parseInt(query);
            return brigadeRepository.findByNameContainingOrSiteIdOrLeaderId("", id, id);
        } catch (NumberFormatException e) {
            // Якщо це не число, шукаємо тільки за назвою
            return brigadeRepository.findByNameContainingOrSiteIdOrLeaderId(query, null, null);
        }
    }
    public void saveBrigade(String name, int siteId, int leaderId) {
        // Створення нового об'єкта бригади
        Brigade brigade = new Brigade();

        // Встановлення значень полів
        brigade.setName(name);
        brigade.setSiteId(siteId);
        brigade.setLeaderId(leaderId);

        // Збереження об'єкта у базі даних
        brigadeRepository.save(brigade);
    }

    public void deleteBrigadeById(int id) {
        brigadeRepository.deleteById(id);
    }
}
