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
        if ("estimate".equals(selectedQuery)) {
            model.addAttribute("projects", projectService.getAllProjects());
        } else if ("brigade".equals(selectedQuery)) {
            model.addAttribute("workTypes", workTypeService.getAllWorkTypes());
        } else if ("brigadesAndLeaders".equals(selectedQuery)) {
            List<Object[]> brigades = brigadeService.findAllBrigadesAndLeaders();
            model.addAttribute("brigades", brigades);
        } else if ("overbudgetMaterials".equals(selectedQuery)) {
            model.addAttribute("projects", projectService.getAllProjects());
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

}
