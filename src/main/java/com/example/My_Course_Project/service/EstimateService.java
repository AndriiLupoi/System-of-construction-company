package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Estimate;
import com.example.My_Course_Project.repository.EstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository estimateRepository;

    public List<Estimate> getAllEstimates() {
        return estimateRepository.findAll();
    }
}
