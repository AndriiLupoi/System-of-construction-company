package com.example.My_Course_Project.controller;

import org.springframework.ui.Model;
import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.service.KeysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class PasswordController {

    @Autowired
    private KeysService keysService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // Return the HTML file name without extension
    }

    @PostMapping("/check-user")
    public String checkUser(@RequestParam("login") String login, @RequestParam("email") String email, Model model) {
        Keys user = keysService.findByLoginAndEmail(login, email);

        // Store input data in the model
        model.addAttribute("login", login);
        model.addAttribute("email", email);

        if (user != null) {
            String generatedPassword = generateRandomPassword(12); // Generate a new password
            model.addAttribute("generatedPassword", generatedPassword); // Add generated password to the model
            model.addAttribute("showFields", true); // Set to show the password fields
            return "forgot-password"; // Return HTML template
        } else {
            model.addAttribute("error", "Користувача не знайдено.");
            return "forgot-password"; // Return HTML template
        }
    }

    private String generateRandomPassword(int length) {
        // Define characters to use in password generation
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam("login") String login,
                                 @RequestParam("newPassword") String newPassword,
                                 Model model) {
        // Attempt to update the password using your service method
        boolean updated = keysService.updatePassword(login, newPassword);
        if (updated) {
            return "redirect:/login"; // Redirect to the login page after successful update
        } else {
            model.addAttribute("error", "Помилка при оновленні паролю.");
            return "forgot-password"; // Return to the forgot password page to show the error
        }
    }
    @PostMapping("/save-password")
    public String savePassword(@RequestParam("login") String login,
                               @RequestParam("newPassword") String newPassword,
                               Model model) {
        // Attempt to save the new password using your service method
        boolean updated = keysService.updatePassword(login, newPassword);
        if (updated) {
            return "redirect:/login"; // Redirect to the login page after successful save
        } else {
            model.addAttribute("error", "Помилка при збереженні паролю.");
            return "forgot-password"; // Return to the forgot password page to show the error
        }
    }

}
