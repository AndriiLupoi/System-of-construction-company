package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.BuildingManagement;
import com.example.My_Course_Project.repository.BuildingManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingManagementService {

    @Autowired
    private BuildingManagementRepository buildingManagementRepository;

    public List<BuildingManagement> getAllBuildings() {
        return buildingManagementRepository.findAll();
    }
}
