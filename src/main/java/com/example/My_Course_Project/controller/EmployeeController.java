package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.model.Employee;
import com.example.My_Course_Project.model.JobCategory;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;


@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JobCategoryService jobCategoryService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private BrigadeService brigadeService;
    @Autowired
    private KeysService keysService;

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

    @GetMapping("/employee")
    public String showEmployeeForm(@PathVariable(required = false) Integer id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Перенаправлення на логін, якщо користувач не аутентифікований
        }

        keysService.setUserRoles(model, session);

        // Якщо id передано, це редагування існуючого працівника
        if (id != null) {
            Employee employee = employeeService.findEmployeeById(id);
            model.addAttribute("employee", employee);
        } else {
            // Якщо id не передано, створюємо новий об'єкт для додавання працівника
            model.addAttribute("employee", new Employee());
        }

        // Отримання даних для випадаючих списків
        List<JobCategory> jobCategories = jobCategoryService.getAllJobCategory();
        List<Site> sites = siteService.getAllSites();
        List<Brigade> brigades = brigadeService.getAllBrigades();

        model.addAttribute("jobCategories", jobCategories);
        model.addAttribute("sites", sites);
        model.addAttribute("brigades", brigades);

        return "crudEmployee";  // Повертаємо назву шаблону для редагування або додавання
    }



    @GetMapping("/employee/edit")
    public String editEmployeeForm(@RequestParam("id") int id, Model model, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        keysService.setUserRoles(model, session);

        // Отримання працівника за ID
        Employee employee = employeeService.getAllEmployees().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Отримання додаткових даних, якщо потрібно (наприклад, категорії робіт, сайти, бригади)
        List<JobCategory> jobCategories = jobCategoryService.getAllJobCategory();
        List<Site> sites = siteService.getAllSites();
        List<Brigade> brigades = brigadeService.getAllBrigades();

        // Додаємо працівника та списки категорій, сайтів та бригад до моделі
        model.addAttribute("employee", employee);
        model.addAttribute("jobCategories", jobCategories);
        model.addAttribute("sites", sites);
        model.addAttribute("brigades", brigades);

        return "crudEmployee"; // Повертаємо назву шаблону для редагування
    }

    @PostMapping("/employee/edit")
    public String updateEmployee(@RequestParam("id") int id,
                                 @RequestParam("name") String name,
                                 @RequestParam("position") String position,
                                 @RequestParam("jobCategory.id") Integer jobCategoryId,
                                 @RequestParam("site.id") Integer siteId,
                                 @RequestParam("brigade.id") Integer brigadeId,
                                 @RequestParam(value = "image", required = false) MultipartFile imageFile,
                                 HttpSession session,
                                 Model model) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        keysService.setUserRoles(model, session);
        // Отримання працівника за ID
        Employee employee = employeeService.findEmployeeById(id);
        employee.setName(name);
        employee.setPosition(position);

        // Зв'язок із JobCategory, Site, та Brigade
        if (jobCategoryId != null) {
            JobCategory jobCategory = jobCategoryService.findJobCategoryById(jobCategoryId);
            employee.setJobCategory(jobCategory);
        }
        if (siteId != null) {
            Site site = siteService.findSiteById(siteId);
            employee.setSite(site);
        }
        if (brigadeId != null) {
            Brigade brigade = brigadeService.findBrigadeById(brigadeId);
            employee.setBrigade(brigade);
        }

        // Оновлення зображення, якщо воно завантажене
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] imageData = imageFile.getBytes();
                employee.setImage(imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Збереження оновленого працівника
        employeeService.updateEmployee(employee);

        return "redirect:/employees"; // Повертає до списку працівників після збереження
    }

    @PostMapping("/employee/delete")
    public String deleteEmployee(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployeeById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Працівника видалено!");
        return "redirect:/employees";
    }
}
