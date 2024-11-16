package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Project;
import com.example.My_Course_Project.service.ProjectService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/project/image/{id}")
    public ResponseEntity<byte[]> getProjectImage(@PathVariable int id) {
        Project project = projectService.getProjectById(id);
        byte[] image = project.getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    @PostMapping("/project/upload")
    public String uploadProjectImage(@RequestParam("id") int id,
                                     @RequestParam("image") MultipartFile image,
                                     HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        try {
            // Перевірка, що зображення не порожнє
            if (image != null && !image.isEmpty()) {
                projectService.saveProjectImage(id, image.getBytes()); // Викликаємо метод для збереження зображення
            }
            return "redirect:/home"; // Перенаправлення на список проектів
        } catch (IOException e) {
            e.printStackTrace(); // Виводимо стек помилок в консоль
            return "error"; // Обробка помилок
        }
    }

    @GetMapping("/project/edit")
    public String editProjectImage(@RequestParam("id") int id, Model model, HttpSession session) {
        // Перевірка наявності користувача в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        // Отримання проекту за ID
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project); // Додаємо проект до моделі для відображення в формі
        return "editProject"; // Повертаємо назву шаблону для редагування
    }
}
