package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.service.KeysService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private KeysService userService;


    @PostMapping("/addUser/save")
    public String addUser(
            @ModelAttribute("user") Keys user,
            @RequestParam("position") String position,
            @RequestParam("allowedTables") String allowedTables,
            @RequestParam(value = "allowedFields", required = false) List<String> allowedFields,
            Model model,
            HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }
        userService.setUserRoles(model, session);

        // Додаємо список доступних таблиць у модель
        model.addAttribute("tables", getAvailableTables());

        // Перевіряємо позицію та таблицю, щоб показати додаткові поля для "оператора" та таблиці "бригади"
        boolean showFields = "оператор".equals(position) && "brigade".equals(allowedTables);
        model.addAttribute("showFields", showFields);

        // Додаємо необхідні поля для вибору, якщо потрібно
        if (showFields) {
            model.addAttribute("fields", List.of("name", "siteId", "leaderId")); // Поля для таблиці "бригади"
        }

        // Зберігаємо вибрані поля, якщо вони є
        String allowedFieldsStr = (allowedFields != null && !allowedFields.isEmpty()) ? String.join(",", allowedFields) : "";
        user.setAllowedFields(allowedFieldsStr);

        if (allowedTables != null && position != null && !allowedFieldsStr.isEmpty()) {
            userService.saveUser(user);
            return "redirect:/home"; // Повернення до списку користувачів
        } else if (allowedTables != null && position != null) {
            userService.saveUser(user);
            return "redirect:/home";

        }

        // Повертаємо вже заповнені дані в модель, якщо є помилки
        model.addAttribute("user", user);
        model.addAttribute("allowedTables", allowedTables);
        model.addAttribute("allowedFields", allowedFields); // Щоб зберегти вибрані поля

        return "userAdd"; // Повертаємося до форми з уже введеними даними
    }



    public List<String> getAvailableTables() {
        return List.of("project", "equipment", "category", "brigade", "building_management", "estimate",
                "report", "schedule", "site", "work_type", "jobCategory");
    }
}
