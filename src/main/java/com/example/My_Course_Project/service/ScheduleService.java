package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Schedule;
import com.example.My_Course_Project.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Логіка пошуку розкладів
    public List<Schedule> searchSchedules(String query) {
        try {
            Integer id = Integer.parseInt(query);
            return scheduleRepository.findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(id, id, null, null);
        } catch (NumberFormatException e) {
            // Якщо не число, спробуйте розпізнати як дату
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Задайте формат дати
                Date date = dateFormat.parse(query);

                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return scheduleRepository.findByProjectIdOrWorkTypeIdOrStartDateOrEndDate(null, null, localDate, localDate);
            } catch (ParseException ex) {
                // Тут ви можете обробити інші значення, наприклад, якщо ви хочете шукати за іншим критерієм
                throw new RuntimeException("Invalid search query: " + query, ex);
            }
        }
    }
}
