package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.BuildingManagement;
import com.example.My_Course_Project.repository.BuildingManagementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuildingManagementService {

    @Autowired
    private BuildingManagementRepository buildingManagementRepository;

    public List<BuildingManagement> getAllBuildings() {
        return buildingManagementRepository.findAll();
    }

    // Логіка пошуку управлінь будівництва
    public List<BuildingManagement> searchBuildingManagements(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return buildingManagementRepository.findByNameContaining(id.toString());
        } catch (NumberFormatException e) {
            return buildingManagementRepository.findByNameContaining(query);
        }
    }

    public void deleteBuildingManagementById(int id) {
        buildingManagementRepository.deleteById(id);
    }

    public void saveBuildingManagement(String name) {
        BuildingManagement buildingManagement = new BuildingManagement();

        buildingManagement.setName(name);

        buildingManagementRepository.save(buildingManagement);
    }

    public BuildingManagement findBuildingById(int id) {
        return buildingManagementRepository.findById(id).orElseThrow(() -> new RuntimeException("Building Managment not found"));
    }
    public void updateBuildingById(int id, BuildingManagement building) {
        Optional<BuildingManagement> existingBuilding = buildingManagementRepository.findById(id);

        if (existingBuilding.isPresent()) {
            BuildingManagement buildingToUpdate = existingBuilding.get();
            buildingToUpdate.setName(building.getName());
            buildingManagementRepository.save(buildingToUpdate);
        } else {
            throw new EntityNotFoundException("Building with id " + id + " not found");
        }
    }
}
