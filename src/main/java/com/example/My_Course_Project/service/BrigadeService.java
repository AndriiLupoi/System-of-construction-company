package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.model.Schedule;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.repository.BrigadeRepository;
import com.example.My_Course_Project.repository.ScheduleRepository;
import com.example.My_Course_Project.repository.SiteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BrigadeService {

    @Autowired
    private BrigadeRepository brigadeRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

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

    public void saveBrigade(String name, Integer siteId, Integer leaderId) {
        // Створення нового об'єкта бригади з використанням конструктора
        Brigade brigade = new Brigade();
        brigade.setName(name);

        // Знаходження сайту
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Розташування не знайдено"));

        brigade.setSiteId(siteId);  // Використання сеттера для siteId
        brigade.setLeaderId(leaderId); // Використання сеттера для leaderId

        // Збереження об'єкта у базі даних
        brigadeRepository.save(brigade);
    }


    public void deleteBrigadeById(int id) {
        brigadeRepository.deleteById(id);
    }


    public List<Object[]> findAllBrigadesAndLeaders() {
        Set<Object[]> resultSet = new HashSet<>(); // Використовуємо Set для унікальності

        // Отримуємо всіх бригад
        List<Brigade> brigades = brigadeRepository.findAll();

        for (Brigade brigade : brigades) {
            // Отримуємо перший розклад, пов'язаний з бригадою
            List<Schedule> schedules = scheduleRepository.findByBrigade(brigade);

            // Якщо є хоча б один розклад, додаємо до результату
            if (!schedules.isEmpty()) {
                Schedule schedule = schedules.get(0); // Отримуємо перший розклад
                resultSet.add(new Object[]{
                        brigade.getName(),
                        schedule.getProject().getName(),
                        brigade.getLeader().getName()
                });
            }
        }

        return new ArrayList<>(resultSet); // Повертаємо список результатів
    }

    public Brigade findBrigadeById(int id) {
        return brigadeRepository.findById(id).orElseThrow(() -> new RuntimeException("Brigade not found"));
    }

    public void updateBrigadeById(int id, Brigade brigade) {
        Optional<Brigade> existingBrigade = brigadeRepository.findById(id);

        if (existingBrigade.isPresent()) {
            Brigade brigadeToUpdate = existingBrigade.get();
            brigadeToUpdate.setName(brigade.getName());
            brigadeToUpdate.setSiteId(brigade.getSiteId());
            brigadeToUpdate.setLeaderId(brigade.getLeaderId());
            brigadeRepository.save(brigadeToUpdate);
        } else {
            throw new EntityNotFoundException("Brigade with id " + id + " not found");
        }
    }

    public Brigade saveBrigade(Brigade brigade) {
        return brigadeRepository.save(brigade);
    }

}
