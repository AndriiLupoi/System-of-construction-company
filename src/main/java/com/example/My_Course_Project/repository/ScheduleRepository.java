package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(Integer projectId, Integer workTypeId, LocalDate startDate, LocalDate endDate);
}
