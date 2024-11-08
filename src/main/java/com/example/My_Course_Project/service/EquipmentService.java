package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Equipment;
import com.example.My_Course_Project.model.Schedule;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.repository.EquipmentRepository;
import com.example.My_Course_Project.repository.ScheduleRepository;
import com.example.My_Course_Project.repository.SiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private SiteRepository siteRepository;

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public void updateEquipment(Equipment updatedEquipment) {
        // Перевірка, чи існує обладнання з таким ID
        if (equipmentRepository.existsById(updatedEquipment.getId())) {
            equipmentRepository.save(updatedEquipment); // Зберігаємо оновлене обладнання
        } else {
            throw new ResourceNotFoundException("Обладнання не знайдено з ID: " + updatedEquipment.getId());
        }
    }

    public List<Equipment> searchEquipment(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return equipmentRepository.findByNameContainingOrTypeContainingOrSiteId("", "", id);
        } catch (NumberFormatException e) {
            return equipmentRepository.findByNameContainingOrTypeContainingOrSiteId(query, query, null);
        }
    }

    public void saveEquipment(String name, String type, int siteId) {
        Equipment equipment = new Equipment();

        equipment.setName(name);
        equipment.setType(type);
        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid site ID: " + siteId));
        equipment.setSite(site);

        equipmentRepository.save(equipment);
    }

    public void deleteEquipmentById(int id) {
        equipmentRepository.deleteById(id);
    }

    public List<Object[]> findEquipmentBySiteAndDate(Integer siteId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> equipmentList = new ArrayList<>();

        List<Equipment> equipmentItems = equipmentRepository.findBySiteId(siteId);

        if (startDate == null || endDate == null) {
            for (Equipment equipment : equipmentItems) {
                equipmentList.add(new Object[]{
                        equipment.getName(),
                        equipment.getType(),
                        equipment.getSite().getName(),
                        null,
                        null
                });
            }
            return equipmentList;
        }

        List<Schedule> schedules = scheduleRepository.findBySiteId(siteId);

        for (Equipment equipment : equipmentItems) {
            for (Schedule schedule : schedules) {
                if (schedule.getSite().getId() == siteId &&
                        (schedule.getStartDate().isEqual(startDate) || schedule.getStartDate().isAfter(startDate)) &&
                        (schedule.getEndDate().isEqual(endDate) || schedule.getEndDate().isBefore(endDate))) {

                    equipmentList.add(new Object[]{
                            equipment.getName(),
                            equipment.getType(),
                            equipment.getSite().getName(),
                            schedule.getStartDate(),
                            schedule.getEndDate()
                    });
                }
            }
        }

        equipmentList.sort((o1, o2) -> {
            LocalDate startDate1 = (LocalDate) o1[3];
            LocalDate startDate2 = (LocalDate) o2[3];
            return startDate1.compareTo(startDate2);
        });
        return equipmentList;
    }

    public Equipment findEquipmentById(int id) {
        return equipmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Equipment not found"));
    }

    public void updateEquipmentById(int id, Equipment equipment) {
        Optional<Equipment> existingEquipment = equipmentRepository.findById(id);

        if (existingEquipment.isPresent()) {
            Equipment equipmentToUpdate = existingEquipment.get();

            equipmentToUpdate.setName(equipment.getName());
            equipmentToUpdate.setType(equipment.getType());
            equipmentToUpdate.setSite(equipment.getSite()); // Оновлюємо сайт

            equipmentRepository.save(equipmentToUpdate); // Збереження оновленого об'єкта
        } else {
            throw new RuntimeException("Обладнання з ID " + id + " не знайдено.");
        }
    }
}

