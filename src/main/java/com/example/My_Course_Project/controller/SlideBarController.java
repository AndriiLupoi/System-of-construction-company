package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class SlideBarController {


    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private BrigadeService brigadeService;

    @GetMapping("/tables")
    public String showTablesPage(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "tables";
    }

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        List<Project> projects = projectService.getAllProjects(); // Отримуємо всі проекти
        List<Category> categories = categoryService.getAllCategories(); // Отримуємо всі категорії
        List<Site> sites = siteService.getAllSites(); // Отримуємо всі сайти
        List<Brigade> buildingManagements = brigadeService.getAllBrigades(); // Отримуємо всі бригади
        List<Employee> employees = employeeService.getAllEmployees(); // Отримуємо всіх працівників

        // Створюємо мапу для категорій
        Map<Integer, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        // Створюємо мапу для сайтів
        Map<Integer, Site> siteMap = sites.stream()
                .collect(Collectors.toMap(Site::getId, site -> site));

        // Створюємо мапу для бригад
        Map<Integer, Brigade> brigadeMap = buildingManagements.stream()
                .collect(Collectors.toMap(Brigade::getId, brigade -> brigade));

        // Створюємо мапу для працівників
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, employee -> employee));

        // Додаємо всі дані до моделі
        model.addAttribute("projects", projects);
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("brigadeMap", brigadeMap); // Додаємо мапу бригад до моделі
        model.addAttribute("employeeMap", employeeMap); // Додаємо мапу працівників до моделі

        return "home"; // Повертаємо назву шаблону
    }







    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Виходимо із системи, очищаємо сесію
        return "redirect:/login"; // Перенаправлення на сторінку логіну після виходу
    }

    @GetMapping("/employees")
    public String getAllEmployees(Model model, HttpSession session) {
        // Перевірка наявності користувача в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee"; // Повернення шаблону для відображення
    }
}
