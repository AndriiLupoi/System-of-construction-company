package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByProjectIdOrWorkTypeIdOrCompletionDateOrActualMaterialUsedOrActualCost(
            Integer projectId,
            Integer workTypeId,
            Date completionDate,
            String actualMaterialUsed,
            Double actualCost
    );
}