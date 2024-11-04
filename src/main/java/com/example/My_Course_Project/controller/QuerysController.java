package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.DataTransferObjects.BrigadeDetailsDto;
import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.repository.BrigadeRepository;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model; // Змінений імпорт
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuerysController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private EstimateService estimateService;
    @Autowired
    private BrigadeService brigadeService;
    @Autowired
    private BrigadeRepository brigadeRepository;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private BuildingManagementService buildingManagementService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private EmployeeService employeeService;

    private boolean isUserAuthenticated(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @PostMapping("/selectedQuery")
    public String handleQuerySelection(@RequestParam String selectedQuery,
                                       Model model, HttpSession session) {
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }

        // Логування отриманих параметрів
        System.out.println("Selected Query: " + selectedQuery);

        model.addAttribute("selectedQuery", selectedQuery);

        switch (selectedQuery) {
            case "estimate":
                model.addAttribute("projects", projectService.getAllProjects());
                break;

            case "brigade":
                model.addAttribute("workTypes", workTypeService.getAllWorkTypes());
                break;

            case "brigadesAndLeaders":
                List<Object[]> brigades = brigadeService.findAllBrigadesAndLeaders();
                model.addAttribute("brigades", brigades);
                break;

            case "overbudgetMaterials":
                model.addAttribute("projectsForMaterial", projectService.getAllProjects());
                break;

            case "worksByBrigades":
                model.addAttribute("brigades", brigadeService.getAllBrigades());
                break;

            case "schedulesByManagementOrSite":
                model.addAttribute("managements", buildingManagementService.getAllBuildings());
                model.addAttribute("sites", siteService.getAllSites());
                break;

            case "brigadeCompositionForProject":
                model.addAttribute("projectsForBrigades", projectService.getAllProjects());
                break;

            case "engineeringAndTechnicalStaff":
                model.addAttribute("managements", buildingManagementService.getAllBuildings());
                model.addAttribute("sites", siteService.getAllSites());
                break;

            default:
                model.addAttribute("error", "Такого запиту не існує!");
                return "error"; // Створіть сторінку для відображення помилок
        }
        return "querys";
    }


    @PostMapping("/EstimateAndSchedule")
    public String getProjectDetails(@RequestParam String projectName,
                                    @RequestParam String selectedQuery,
                                    Model model,
                                    HttpSession session) {
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        model.addAttribute("schedules", scheduleService.getSchedulesByProjectName(projectName));
        model.addAttribute("estimates", estimateService.getEstimatesByProjectName(projectName));
        model.addAttribute("projectName", projectName);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("selectedQuery", selectedQuery);

        return "querys";
    }

    @PostMapping("/selectWork")
    public String handleProjectSelection(@RequestParam int workTypeId,
                                         @RequestParam String workTypeName,
                                         @RequestParam String selectedQuery,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                         Model model,
                                         HttpSession session)  {
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }
        // Отримуємо бригади за вказаним типом робіт та періодом
        List<Object[]> results = scheduleService.findBrigadesByWorkTypeAndPeriod(workTypeId, startDate, endDate);

        model.addAttribute("startDate", startDate);
        model.addAttribute("workTypeName", workTypeName);
        model.addAttribute("endDate", endDate);
        model.addAttribute("results", results);
        model.addAttribute("selectedQuery", selectedQuery);

        return "querys"; // Повертаємо до шаблону, де відобразимо результати
    }

    @PostMapping("/overbudgetMaterials")
    public String getOverBudgetMaterials(
            @RequestParam("projectId") int projectId,
            Model model) {

        List<Object[]> overBudgetMaterials = projectService.findOverBudgetMaterials(projectId);

        // Перевірте, чи отримано дані
        if (overBudgetMaterials.isEmpty()) {
            System.out.println("No over budget materials found for project ID: " + projectId);
        } else {
            System.out.println("OverBudget Materials: " + overBudgetMaterials);
        }

        // Додайте список до моделі
        model.addAttribute("overBudgetMaterials", overBudgetMaterials);

        return "querys"; // Повертає назву шаблону для відображення
    }


    @PostMapping("/worksByBrigades")
    public String handleBrigadeWorkSelection(@RequestParam int brigadeId,
                                             @RequestParam(value = "brigadeName", required = false) String brigadeName,
                                             @RequestParam String selectedQuery,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDateB,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDateB,
                                             Model model,
                                             HttpSession session) {
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }

        // Отримуємо дані про роботи, виконані бригадою за вказаний період
        List<Object[]> brigadeWorkDetails = scheduleService.findWorkDetailsByBrigadeAndPeriod(brigadeId, startDateB, endDateB);

        // Передаємо дані до моделі
        model.addAttribute("startDate", startDateB);
        model.addAttribute("brigadeName", brigadeName);
        model.addAttribute("endDate", endDateB);
        model.addAttribute("brigadeWorkDetails", brigadeWorkDetails);
        model.addAttribute("selectedQuery", selectedQuery);

        return "querys"; // Повертаємо до шаблону для відображення результатів
    }

    @PostMapping("/schedulesByManagementOrSite")
    public String getConstructionProjects(
            @RequestParam(value = "managementId", required = false) Integer managementId,
            @RequestParam(value = "siteId", required = false) Integer siteId,
            Model model,
            HttpSession session) {

        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }

        if (managementId == null && siteId == null) {
            model.addAttribute("error", "Будь ласка, оберіть будівельне управління або ділянку.");
            return "querys";
        }

        List<Object[]> schedulesByManagementOrSite = scheduleService.findConstructionProjectsByManagementOrSite(managementId, siteId);

        model.addAttribute("schedulesByManagementOrSite", schedulesByManagementOrSite);

        return "querys";
    }

    @PostMapping("/brigadeCompositionForProject")
    public String getBrigadeCompositionForProject(
            @RequestParam("projectId") int projectId,
            Model model,
            HttpSession session) {
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }

        List<Object[]> brigadeComposition = employeeService.findBrigadeCompositionForProject(projectId);
        model.addAttribute("brigadeComposition", brigadeComposition);
        return "querys";
    }

    @PostMapping("/engineeringAndTechnicalStaff")
    public String getSpecialistsByCategoryAndLocation(
            @RequestParam(value = "siteId", required = false) Integer  siteId,
            @RequestParam(value = "managementId", required = false) Integer  managementId,
            Model model,
            HttpSession session) {
        // Перевірка аутентифікації
        if (!isUserAuthenticated(session)) {
            return "redirect:/login";
        }

        // Перевірка, чи передано хоча б один параметр
        if (siteId != null || managementId != null) {
            List<Object[]> specialists = employeeService.findEngineeringStaffBySiteOrManagement(siteId, managementId);
            model.addAttribute("specialists", specialists);
        }
        return "querys";
    }


}
