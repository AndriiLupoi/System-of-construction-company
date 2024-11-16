package com.example.My_Course_Project.controller;

import org.springframework.ui.Model;
import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.service.EmailService;
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

    @Autowired
    private EmailService emailService;

    // Метод для відображення сторінки відновлення паролю
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // Повертаємо сторінку forgot-password.html
    }

    @PostMapping("/check-and-verify")
    public String checkAndVerify(@RequestParam("login") String login,
                                 @RequestParam(value = "email", required = false) String email,
                                 @RequestParam(value = "enteredCode", required = false) String enteredCode,
                                 Model model) {
        // Якщо вказано введений код, значить, ми перевіряємо код
        if (enteredCode != null) {
            // Отримуємо користувача з бази даних за логіном
            Keys user = keysService.findByLoginAndEmail(login, email);
            if (user != null) {
                System.out.println("Код в базі: " + user.getVerificationCode());
                System.out.println("Введений код: " + enteredCode);

                // Перевіряємо верифікаційний код
                if (enteredCode.equals(user.getVerificationCode())) {
                    String generatedPassword = generateRandomPassword(12);
                    System.out.println(generatedPassword);
                    model.addAttribute("login", user.getLogin());
                    model.addAttribute("email", user.getEmail());
                    model.addAttribute("generatedPassword", generatedPassword);
                    model.addAttribute("showPasswordField", true);
                    return "forgot-password"; // Повертаємося на ту ж сторінку
                } else {
                    model.addAttribute("error", "Неправильний код підтвердження.");
                    model.addAttribute("showVerificationField", true);
                    return "forgot-password"; // Повертаємося на ту ж сторінку
                }
            } else {
                model.addAttribute("error", "Користувача не знайдено.");
                return "forgot-password"; // Повертаємося на ту ж сторінку
            }
        } else {
            // Якщо введений код не передано, значить, перевіряємо користувача
            Keys user = keysService.findByLoginAndEmail(login, email);
            if (user != null) {
                // Генерація верифікаційного коду
                String verificationCode = generateVerificationCode();
                keysService.saveVerificationCode(login, verificationCode); // Зберігаємо код у базі

                // Надсилаємо код на пошту
                emailService.sendVerificationCode(email, verificationCode);

                model.addAttribute("login", login);
                model.addAttribute("email", email);
                model.addAttribute("showVerificationField", true);
                return "forgot-password"; // Повертаємося на ту ж сторінку
            } else {
                model.addAttribute("error", "Користувача не знайдено. Перевірте логін та електронну пошту.");
                return "forgot-password"; // Повертаємося на ту ж сторінку
            }
        }
    }

    // Збереження нового паролю
    @PostMapping("/save-password")
    public String savePassword(@RequestParam("login") String login,
                               @RequestParam("newPassword") String newPassword,
                               Model model) {
        boolean updated = keysService.updatePassword(login, newPassword);
        if (updated) {
            return "redirect:/login"; // Перенаправляємо на сторінку логіну
        } else {
            model.addAttribute("error", "Помилка при збереженні паролю.");
            return "forgot-password"; // Повертаємо сторінку з помилкою
        }
    }

    // Генерація верифікаційного коду
    private String generateVerificationCode() {
        return String.valueOf((int) (Math.random() * 900000) + 100000); // Генеруємо 6-значний код
    }

    // Генерація випадкового пароля
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }
}
