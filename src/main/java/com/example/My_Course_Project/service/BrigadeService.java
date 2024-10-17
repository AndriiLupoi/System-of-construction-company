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
}
