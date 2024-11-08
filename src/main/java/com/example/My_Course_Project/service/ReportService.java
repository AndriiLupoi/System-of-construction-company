package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.Report;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.model.WorkType;
import com.example.My_Course_Project.repository.ProjectRepository;
import com.example.My_Course_Project.repository.ReportRepository;
import com.example.My_Course_Project.repository.WorkTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkTypeRepository workTypeRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // Логіка пошуку звітів
    public List<Report> searchReports(String query) {
        List<Report> results = new ArrayList<>();

        // Спробуйте спочатку розпізнати як число (ID проекту або типу роботи)
        try {
            Integer id = Integer.parseInt(query);
            results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrMaterialOrUsedMaterialOrActualCost(id, id, null, "", 0, 0.0));
        } catch (NumberFormatException e) {
            // Якщо не число, спробуйте розпізнати як дату
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Задайте формат дати
                Date date = dateFormat.parse(query);
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrMaterialOrUsedMaterialOrActualCost(null, null, date, "",0, 0.0));
            } catch (ParseException pe) {
                // Якщо це не число і не дата, пробуємо шукати за фактичним матеріалом
                String material = query;
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrMaterialOrUsedMaterialOrActualCost(null, null, null, material,0, 0.0));
            }

            // Спробуйте знайти за вартістю
            double cost = parseCost(query); // Перетворення query у вартість
            if (cost >= 0) {
                results.addAll(reportRepository.findByProjectIdOrWorkTypeIdOrCompletionDateOrMaterialOrUsedMaterialOrActualCost(null, null, null, "",0, cost));
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

    public void saveReport(int projectId, int workTypeId, LocalDate completionDate, String Material, Integer UsedMaterial , double actualCost) {
        // Створення нового об'єкта Report
        Report report = new Report();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));

        WorkType workType = workTypeRepository.findById(workTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid workType ID: " + workTypeId));


        // Встановлення значень полів
        report.setProject(project);
        report.setWorkType(workType);
        report.setCompletionDate(completionDate);
        report.setMaterial(Material);
        report.setUsedMaterial(UsedMaterial);
        report.setActualCost(actualCost);

        // Збереження об'єкта у базі даних
        reportRepository.save(report);
    }

    public void deleteReportById(int id) {
        reportRepository.deleteById(id);
    }

    public Report findReportById(int id) {
        return reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid report ID: " + id));
    }

    public void updateReportById(int id, Report report) {
        Optional<Report> existingReport = reportRepository.findById(id);

        if (existingReport.isPresent()) {
            Report reportToUpdate = existingReport.get();
            reportToUpdate.setProject(report.getProject());
            reportToUpdate.setWorkType(report.getWorkType());
            reportToUpdate.setCompletionDate(report.getCompletionDate());
            reportToUpdate.setMaterial(report.getMaterial());
            reportToUpdate.setUsedMaterial(report.getUsedMaterial());
            reportToUpdate.setActualCost(report.getActualCost());
            reportRepository.save(reportToUpdate);
        } else {
            throw new EntityNotFoundException("Report with id " + id + " not found");
        }
    }


}
