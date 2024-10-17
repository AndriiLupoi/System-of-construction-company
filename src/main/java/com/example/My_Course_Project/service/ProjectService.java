package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional
    public void updateProject(Project updatedProject) {
        // Перевірка, чи проект існує перед оновленням
        if (!projectRepository.existsById(updatedProject.getId())) {
            throw new ResourceNotFoundException("Project not found with ID: " + updatedProject.getId());
        }

        // Завантаження проекту з бази даних
        Project existingProject = projectRepository.findById(updatedProject.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + updatedProject.getId()));

        // Оновлення полів проекту
        existingProject.setName(updatedProject.getName());
        existingProject.setCategoryId(updatedProject.getCategoryId());
        existingProject.setSiteId(updatedProject.getSiteId());

        // Збереження оновленого проекту
        projectRepository.save(existingProject);
    }

}
