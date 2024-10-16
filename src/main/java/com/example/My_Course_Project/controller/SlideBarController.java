package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.service.KeysService;
import com.example.My_Course_Project.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SlideBarController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public String showProjectsPage(Model model, HttpSession session) {
        // Перевіряємо, чи є користувач в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        List<Project> projects = projectService.getAllProjects(); // Отримуємо всі проекти
        model.addAttribute("projects", projects); // Додаємо проекти до моделі
        return "projects"; // Повертаємо шаблон для відображення проектів
    }

    @GetMapping("/home")
    public String showHomePage(Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Виходимо із системи, очищаємо сесію
        return "redirect:/login"; // Перенаправлення на сторінку логіну після виходу
    }
}
