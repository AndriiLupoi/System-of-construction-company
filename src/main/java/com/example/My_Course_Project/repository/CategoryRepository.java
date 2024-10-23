package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByNameContainingOrDescriptionContaining(String name, String description);
}
