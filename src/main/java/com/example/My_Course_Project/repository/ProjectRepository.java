package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByNameContainingOrCategoryIdOrSiteId(String name, Integer categoryId, Integer siteId);

    @Override
    Project save(Project project);
}
