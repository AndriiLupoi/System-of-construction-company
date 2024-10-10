package com.example.My_Course_Project.controller;

import org.springframework.ui.Model;
import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.service.KeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PasswordController {

    @Autowired
    private KeysService keysService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // Повертаємо назву HTML-файлу без розширення
    }

    @PostMapping("/check-user")
    public String checkUser(@RequestParam("login") String login, @RequestParam("email") String email, Model model) {
        Keys user = keysService.findByLoginAndEmail(login, email);

        // Зберігаємо введені дані в модель
        model.addAttribute("login", login);
        model.addAttribute("email", email);

        if (user != null) {
            model.addAttribute("showFields", true); // Додаємо параметр showFields
            return "forgot-password"; // Повертаємо HTML-шаблон
        } else {
            model.addAttribute("error", "Користувача не знайдено.");
            return "forgot-password"; // Повертаємо HTML-шаблон
        }
    }





    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("login") String login, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Паролі не співпадають.");
            return "change-password";
        }

        boolean updated = keysService.updatePassword(login, newPassword);
        if (updated) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "Помилка при оновленні паролю.");
            return "change-password";
        }
    }
}
