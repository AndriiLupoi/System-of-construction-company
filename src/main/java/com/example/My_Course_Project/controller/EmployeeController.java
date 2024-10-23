package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Employee;
import com.example.My_Course_Project.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee/image/{id}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable int id) {
        Employee employee = employeeService.getEmployeeById(id);
        byte[] image = employee.getImage(); // або де ви зберігаєте зображення в базі
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG); // Встановіть правильний тип зображення
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }



    @PostMapping("/employee/upload")
    public String uploadEmployeeImage(@RequestParam("id") int id,
                                      @RequestParam("image") MultipartFile image,
                                      HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        try {
            // Перевірка, що зображення не порожнє
            if (image != null && !image.isEmpty()) {
                employeeService.saveEmployeeImage(id, image.getBytes()); // Викликаємо метод для збереження зображення
            }
            return "redirect:/employees"; // Перенаправлення на список працівників
        } catch (IOException e) {
            e.printStackTrace(); // Виводимо стек помилок в консоль
            return "error"; // Обробка помилок
        }
    }


    @GetMapping("/employee/edit")
    public String editEmployeeImage(@RequestParam("id") int id, Model model, HttpSession session) {
        // Перевірка наявності користувача в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        // Отримання працівника за ID
        Employee employee = employeeService.getAllEmployees().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found")); // Пошук працівника за ID

        model.addAttribute("employee", employee); // Додаємо працівника до моделі для відображення в формі
        return "editEmployee"; // Повертаємо назву шаблону для редагування
    }



}
