package com.example.My_Course_Project.service;

import com.example.My_Course_Project.DataTransferObjects.ProjectDTO;
import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<ProjectDTO> getAllProjectsWithoutImages() {
        // Отримуємо всі проекти
        List<Project> projects = projectRepository.findAll();

        // Перетворюємо їх на DTO без поля "image"
        return projects.stream()
                .map(project -> new ProjectDTO(
                        project.getId(),
                        project.getName(),
                        project.getCategoryId(),
                        project.getSiteId(),
                        project.getStartDate(),
                        project.getEndDate()
                ))
                .collect(Collectors.toList());
    }

    public void saveProjectImage(int id, byte[] image) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found")); // Пошук проекту за ID
        project.setImage(image); // Зберігаємо зображення
        projectRepository.save(project); // Оновлюємо запис у базі даних
    }

    public Project getProjectById(int projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект не знайдено з ID: " + projectId));
    }

    // Логіка пошуку проектів
    public List<Project> searchProjects(String query) {
        try {
            // Якщо query — це числове значення (наприклад, для ID категорії або сайту)
            Integer id = Integer.parseInt(query);
            return projectRepository.findByNameContainingOrCategoryIdOrSiteId("", id, id);
        } catch (NumberFormatException e) {
            // Якщо це не число, шукаємо тільки за назвою
            return projectRepository.findByNameContainingOrCategoryIdOrSiteId(query, null, null);
        }
    }
}
