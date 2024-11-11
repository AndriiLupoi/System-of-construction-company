package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.service.KeysService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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

        Keys currentUser = (Keys) session.getAttribute("user");

        // Додаємо список доступних таблиць у модель
        model.addAttribute("tables", getAvailableTables(currentUser));

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
            return "redirect:/home";
        }

        // Повертаємо вже заповнені дані в модель, якщо є помилки
        model.addAttribute("user", user);
        model.addAttribute("allowedTables", allowedTables);
        model.addAttribute("allowedFields", allowedFields); // Щоб зберегти вибрані поля

        return "userAdd"; // Повертаємося до форми з уже введеними даними
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
