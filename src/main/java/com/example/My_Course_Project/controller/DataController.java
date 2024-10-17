package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
@Controller
public class DataController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private BuildingManagementService buildingManagementService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private WorkTypeService workTypeService;


    @GetMapping("/data")
    public String viewTable(@RequestParam("tableName") String tableName, Model model, HttpSession session) {
        // Перевіряємо, чи є користувач в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        switch (tableName) {
            case "projects":
                List<Project> projects = projectService.getAllProjects();
                System.out.println("Loaded projects: " + projects); // Додайте цю строку
                model.addAttribute("data", projects);
                model.addAttribute("tableName", "project");
                break;

            case "equipment":
                List<Equipment> equipment = equipmentService.getAllEquipment();
                model.addAttribute("data", equipment);
                model.addAttribute("tableName", "equipment");
                break;

            case "category":
                List<Category> categories = categoryService.getAllCategories(); // Потрібен сервіс для категорій
                model.addAttribute("data", categories);
                model.addAttribute("tableName", "category");
                break;

            case "brigade":
                List<Brigade> brigades = brigadeService.getAllBrigades(); // Потрібен сервіс для бригад
                model.addAttribute("data", brigades);
                model.addAttribute("tableName", "brigade");
                break;

            case "building_management":
                List<BuildingManagement> buildings = buildingManagementService.getAllBuildings(); // Потрібен сервіс для управлінь будівництвом
                model.addAttribute("data", buildings);
                model.addAttribute("tableName", "building_management");
                break;

            case "estimate":
                List<Estimate> estimates = estimateService.getAllEstimates(); // Потрібен сервіс для кошторисів
                model.addAttribute("data", estimates);
                model.addAttribute("tableName", "estimate");
                break;

            case "report":
                List<Report> reports = reportService.getAllReports(); // Потрібен сервіс для звітів
                model.addAttribute("data", reports);
                model.addAttribute("tableName", "report");
                break;

            case "schedule":
                List<Schedule> schedules = scheduleService.getAllSchedules(); // Потрібен сервіс для розкладів
                model.addAttribute("data", schedules);
                model.addAttribute("tableName", "schedule");
                break;

            case "site":
                List<Site> sites = siteService.getAllSites(); // Потрібен сервіс для сайтів
                model.addAttribute("data", sites);
                model.addAttribute("tableName", "site");
                break;

            case "work_type":
                List<WorkType> workTypes = workTypeService.getAllWorkTypes(); // Потрібен сервіс для типів робіт
                model.addAttribute("data", workTypes);
                model.addAttribute("tableName", "work_type");
                break;

            default:
                model.addAttribute("error", "Таблиця не знайдена");
                return "error"; // Створіть сторінку для відображення помилок
        }
        return "tables"; // Повертаємо шаблон таблиць з даними
    }


}
