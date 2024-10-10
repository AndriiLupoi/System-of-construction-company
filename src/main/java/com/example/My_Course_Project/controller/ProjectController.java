package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.service.KeysService;
import com.example.My_Course_Project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private KeysService keysService;

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("login") String login, @RequestParam("password") String password, HttpSession session) {
        Keys user = keysService.checkLogin(login, password);

        if (user != null) {
            // Зберігаємо користувача в сесії
            session.setAttribute("user", user);
            // Якщо користувач знайдений, перенаправляємо на головну сторінку
            return new ModelAndView("redirect:/index"); // Перенаправлення на сторінку /index
        } else {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("error", "Невірний логін або пароль");
            return modelAndView;
        }
    }



    @GetMapping("/index")
    public String showPage(Model model, HttpSession session) {
        // Перевіряємо, чи є користувач в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        List<Project> projects = projectService.getAllProjects(); // Отримуємо всі проекти
        model.addAttribute("projects", projects); // Додаємо проекти до моделі
        return "home"; // Повертаємо шаблон index
    }


    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Повертаємо сторінку логіну
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Виходимо із системи, очищаємо сесію
        return "redirect:/login"; // Перенаправлення на сторінку логіну після виходу
    }
}
