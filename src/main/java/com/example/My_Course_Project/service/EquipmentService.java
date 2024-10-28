package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Equipment;
import com.example.My_Course_Project.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

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

    // Логіка пошуку обладнання
    public List<Equipment> searchEquipment(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return equipmentRepository.findByNameContainingOrTypeContainingOrSiteId("", "", id);
        } catch (NumberFormatException e) {
            return equipmentRepository.findByNameContainingOrTypeContainingOrSiteId(query, query, null);
        }
    }

    public void saveEquipment(String name, String type, int siteId) {
        // Створення нового об'єкта обладнання
        Equipment equipment = new Equipment();

        // Встановлення значень полів
        equipment.setName(name);
        equipment.setType(type);
        equipment.setSiteId(siteId);

        // Збереження об'єкта у базі даних
        equipmentRepository.save(equipment);
    }

    public void deleteEquipmentById(int id) {
        equipmentRepository.deleteById(id);
    }

}

