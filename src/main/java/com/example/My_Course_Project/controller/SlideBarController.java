package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.repository.KeysRepository;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
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
    private KeysService keysService;
    @Autowired
    private BrigadeService brigadeService;
    @Autowired
    private BuildingManagementService buildingManagementService;
    @Autowired
    private KeysRepository keysRepository;

    @GetMapping("/tables")
    public String showTablesPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        keysService.setUserRoles(model, session);

        Keys currentUser = (Keys) session.getAttribute("user");

        List<String> allowedTables = keysService.getAllowedTablesForUser(currentUser);

        // Передаємо доступні таблиці в модель
        model.addAttribute("allowedTables", allowedTables);

        return "tables";
    }

    @GetMapping("/querys")
    public String showQuerysPage(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        keysService.setUserRoles(model, session);
        return "querys"; // Назва шаблону
    }

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        keysService.setUserRoles(model, session);

        List<Project> projects = projectService.getAllProjects(); // Отримуємо всі проекти
        List<Category> categories = categoryService.getAllCategories(); // Отримуємо всі категорії
        List<Site> sites = siteService.getAllSites(); // Отримуємо всі сайти
        List<Brigade> brigades = brigadeService.getAllBrigades(); // Отримуємо всі бригади
        List<Employee> employees = employeeService.getAllEmployees(); // Отримуємо всіх працівників
        List<BuildingManagement> buildingManagements = buildingManagementService.getAllBuildings();

        // Створюємо мапу для категорій
        Map<Integer, Category> categoryMap = categories.stream()
                .collect(Collectors.toMap(Category::getId, category -> category));

        // Створюємо мапу для сайтів
        Map<Integer, Site> siteMap = sites.stream()
                .collect(Collectors.toMap(Site::getId, site -> site));

        // Створюємо мапу для бригад
        Map<Integer, Brigade> brigadeMap = brigades.stream()
                .collect(Collectors.toMap(Brigade::getId, brigade -> brigade));

        // Створюємо мапу для працівників
        Map<Integer, Employee> employeeMap = employees.stream()
                .collect(Collectors.toMap(Employee::getId, employee -> employee));

        Map<Integer, BuildingManagement> buildingManagementMap = buildingManagements.stream()
                .collect(Collectors.toMap(BuildingManagement::getId, buildingManagement -> buildingManagement));

        // Додаємо всі дані до моделі
        model.addAttribute("projects", projects);
        model.addAttribute("categoryMap", categoryMap);
        model.addAttribute("siteMap", siteMap);
        model.addAttribute("brigadeMap", brigadeMap); // Додаємо мапу бригад до моделі
        model.addAttribute("employeeMap", employeeMap); // Додаємо мапу працівників до моделі
        model.addAttribute("buildingManagementMap", buildingManagementMap);

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
        keysService.setUserRoles(model, session);

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "employee"; // Повернення шаблону для відображення
    }
    @GetMapping("/add_info")
    public String addNewInfoPage(String tableName, Model model, HttpSession session) {
        // Перевірка наявності користувача в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }
        keysService.setUserRoles(model, session);

        Keys currentUser = (Keys) session.getAttribute("user");

        List<String> allowedTables = keysService.getAllowedTablesForUser(currentUser);

        // Передаємо доступні таблиці в модель
        model.addAttribute("allowedTables", allowedTables);

        return "add_info";
    }

    @GetMapping("/addUser")
    public String showAddUserForm(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }
        keysService.setUserRoles(model, session);

        Keys currentUser = (Keys) session.getAttribute("user");

        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("Дозволені таблиці для користувача перед getAvailableTables: " + currentUser.getAllowedTables());


        model.addAttribute("user", new Keys());
        model.addAttribute("tables", getAvailableTables(currentUser));
        model.addAttribute("showFields", false); // Спочатку не показуємо додаткові поля
        model.addAttribute("allowedFields", new ArrayList<>());
        return "userAdd";
    }

    public List<String> getAvailableTables(Keys currentUser) {
        Logger logger = Logger.getLogger(getClass().getName());

        if ("власник".equals(currentUser.getPosition())) {
            List<String> allTables = Arrays.asList(currentUser.getAllowedTables().split(","));
            logger.info("Власник: показуємо всі таблиці: " + allTables);
            return allTables;
        } else if ("адміністратор".equals(currentUser.getPosition()) && currentUser.getAllowedTables() != null) {
            List<String> adminTables = Arrays.asList(currentUser.getAllowedTables().split(","));
            logger.info("Адмін: дозволені таблиці: " + adminTables);
            return adminTables;
        }

        logger.info("Немає дозволених таблиць для користувача " + currentUser.getPosition());
        return List.of();
    }

}
