package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SlideBarController {

    @Autowired
    private ProjectService projectService;

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
        return "home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Виходимо із системи, очищаємо сесію
        return "redirect:/login"; // Перенаправлення на сторінку логіну після виходу
    }
}
