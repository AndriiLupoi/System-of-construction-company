package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    List<Equipment> findByNameContainingOrTypeContainingOrSiteId(String name, String type, Integer siteId);

    List<Equipment> findBySiteId(Integer siteId);
}
