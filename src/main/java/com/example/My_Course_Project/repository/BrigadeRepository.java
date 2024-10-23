package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Brigade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Integer> {
    List<Brigade> findByNameContainingOrSiteIdOrLeaderId(String name, Integer site_id, Integer leaderId);
}
