package com.example.My_Course_Project.service;

import com.example.My_Course_Project.exception.ResourceNotFoundException;
import com.example.My_Course_Project.model.Report;
import com.example.My_Course_Project.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Логіка пошуку звітів
    public List<Report> searchReports(String query) {
        List<Report> results = new ArrayList<>();

        // Спробуйте спочатку розпізнати як число (ID проекту або типу роботи)
        try {
            Integer id = Integer.parseInt(query);
            results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrActualMaterialUsedOrActualCost(id, id, null, "", 0.0));
        } catch (NumberFormatException e) {
            // Якщо не число, спробуйте розпізнати як дату
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Задайте формат дати
                Date date = dateFormat.parse(query);
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrActualMaterialUsedOrActualCost(null, null, date, "", 0.0));
            } catch (ParseException pe) {
                // Якщо це не число і не дата, пробуємо шукати за фактичним матеріалом
                String material = query;
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrActualMaterialUsedOrActualCost(null, null, null, material, 0.0));
            }

            // Спробуйте знайти за вартістю
            double cost = parseCost(query); // Перетворення query у вартість
            if (cost >= 0) {
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrActualMaterialUsedOrActualCost(null, null, null, "", cost));
            }
        }

        return results;
    }

    // Метод для перетворення рядка в подвійну вартість
    private double parseCost(String query) {
        try {
            return Double.parseDouble(query);
        } catch (NumberFormatException e) {
            return -1; // Якщо не вдається перетворити, повертаємо -1
        }
    }
}
