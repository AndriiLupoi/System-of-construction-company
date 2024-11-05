package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.DataTransferObjects.BrigadeDetailsDto;
import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(Integer projectId, Integer workTypeId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT p.id FROM Project p WHERE p.name = ?1")
    Integer findProjectIdByName(String projectName);

    List<Schedule> findByProjectId(Integer projectId);

    List<Schedule> findByBrigade(Brigade brigade);

    List<Schedule> findBySiteIdAndStartDateBetween(Integer siteId, LocalDate startDate, LocalDate endDate);

    List<Schedule> findBySiteId(Integer siteId);
}
