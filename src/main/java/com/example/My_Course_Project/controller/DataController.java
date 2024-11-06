package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.DataTransferObjects.ProjectDTOs.ProjectDTO;
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
    @Autowired
    private JobCategoryService jobCategoryService;
    @Autowired
    private KeysService keysService;


    @GetMapping("/data")
    public String viewTable(@RequestParam("tableName") String tableName, Model model, HttpSession session) {
        // Перевіряємо, чи є користувач в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        keysService.setUserRoles(model, session);

        switch (tableName) {
            case "project":
                List<ProjectDTO> projects = projectService.getAllProjectsWithoutImages();
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

            case "jobCategory":
                List<JobCategory> jobCategories = jobCategoryService.getAllJobCategory();
                model.addAttribute("jobCategories", jobCategories);
                model.addAttribute("tableName", "jobCategory");
                break;
            default:
                model.addAttribute("error", "Таблиця не знайдена");
                return "error";
        }
        return "tables"; // Повертаємо шаблон таблиць з даними
    }


    @GetMapping("/data/search")
    public String searchProjects(@RequestParam("tableName") String tableName,
                                 @RequestParam("query") String query,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін

        }

        switch (tableName){
            case "project":
                List<Project> projects = projectService.searchProjects(query);
                model.addAttribute("data", projects);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "brigade":
                List<Brigade> brigades = brigadeService.searchBrigades(query);
                model.addAttribute("data", brigades);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "building_management":
                List<BuildingManagement> buildingManagements = buildingManagementService.searchBuildingManagements(query);
                model.addAttribute("data", buildingManagements);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "category":
                List<Category> categories = categoryService.searchCategories(query);
                model.addAttribute("data", categories);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "equipment":
                List<Equipment> equipment = equipmentService.searchEquipment(query);
                model.addAttribute("data", equipment);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "estimate":
                List<Estimate> estimates = estimateService.searchEstimates(query);
                model.addAttribute("data", estimates);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "report":
                List<Report> reports = reportService.searchReports(query);
                model.addAttribute("data", reports);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "schedule":
                List<Schedule> schedules = scheduleService.searchSchedules(query);
                model.addAttribute("data", schedules);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "site":
                List<Site> sites = siteService.searchSites(query);
                model.addAttribute("data", sites);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "work_type":
                List<WorkType> workTypes = workTypeService.searchWorkTypes(query);
                model.addAttribute("data", workTypes);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "jobCategory":
                List<JobCategory> jobCategories = jobCategoryService.searchJobCategories(query);
                model.addAttribute("jobCategories", jobCategories);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + tableName);
        }

        return "tables";
    }

    @PostMapping("/data/delete/{id}")
    public String deleteRecord(@RequestParam("tableName") String tableName, @PathVariable("id") int id) {
        switch (tableName) {
            case "project":
                projectService.deleteProjectById(id);
                break;
            case "equipment":
                equipmentService.deleteEquipmentById(id);
                break;
            case "category":
                categoryService.deleteCategoryById(id);
                break;
            case "brigade":
                brigadeService.deleteBrigadeById(id);
                break;
            case "building_management":
                buildingManagementService.deleteBuildingManagementById(id);
                break;
            case "estimate":
                estimateService.deleteEstimateById(id);
                break;
            case "report":
                reportService.deleteReportById(id);
                break;
            case "schedule":
                scheduleService.deleteScheduleById(id);
                break;
            case "site":
                siteService.deleteSiteById(id);
                break;
            case "work_type":
                workTypeService.deleteWorkTypeById(id);
                break;
            case "jobCategory":
                jobCategoryService.deleteJobCategoryById(id);
                break;

            default:
                throw new IllegalArgumentException("Неправильна назва таблиці: " + tableName);
        }
        return "redirect:/data?tableName=" + tableName;
    }



}
