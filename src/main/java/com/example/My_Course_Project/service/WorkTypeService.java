package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.WorkType;
import com.example.My_Course_Project.repository.WorkTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTypeService {

    @Autowired
    private WorkTypeRepository workTypeRepository;

    public List<WorkType> getAllWorkTypes() {
        return workTypeRepository.findAll();
    }
}
