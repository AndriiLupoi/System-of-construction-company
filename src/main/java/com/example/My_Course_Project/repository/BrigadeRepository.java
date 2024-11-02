package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.DataTransferObjects.BrigadeDetailsDto;
import com.example.My_Course_Project.model.Brigade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BrigadeRepository extends JpaRepository<Brigade, Integer> {
    List<Brigade> findByNameContainingOrSiteIdOrLeaderId(String name, Integer site_id, Integer leaderId);

}
