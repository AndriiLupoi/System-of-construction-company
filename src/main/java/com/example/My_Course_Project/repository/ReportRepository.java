package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.model.Report;
import com.example.My_Course_Project.model.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByProjectIdOrWorkTypeIdOrCompletionDateOrMaterialOrUsedMaterialOrActualCost(
            Integer projectId,
            Integer workTypeId,
            Date completionDate,
            String material,
            Integer used,
            Double actualCost
    );

    @Query("SELECT " +
            "    e.material, " +
            "    SUM(r.usedMaterial) AS total_used, " +
            "    SUM(r.actualCost) AS total_actual_cost, " +
            "    MAX(e.cost) AS estimated_cost, " +
            "    SUM(r.actualCost) - MAX(e.cost) AS cost_exceeded " +
            "FROM " +
            "    Report r " +
            "JOIN " +
            "    Project p ON r.project.id = p.id " +
            "JOIN " +
            "    Estimate e ON r.project.id = e.projectId AND r.material = e.material " +
            "WHERE " +
            "    p.id = :projectId " +
            "GROUP BY " +
            "    e.material " +
            "HAVING " +
            "    SUM(r.actualCost) > MAX(e.cost)")
    List<Object[]> findOverBudgetMaterials(@Param("projectId") int projectId);

    List<Report> findByProjectAndWorkType(Project project, WorkType workType);
}