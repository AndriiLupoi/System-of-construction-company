package com.example.My_Course_Project.repository;


import com.example.My_Course_Project.model.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobCategoryRepository extends JpaRepository<JobCategory, Integer> {
    Optional<JobCategory> findById(Integer id);
    List<JobCategory> findByNameContainingOrDescriptionContaining(String name, String description);

}
