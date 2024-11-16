package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.DataTransferObjects.ProjectDTOs.ProjectDTO;
import com.example.My_Course_Project.model.Brigade;
import com.example.My_Course_Project.model.JobCategory;
import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.model.Site;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class AddController {

    private static final Logger logger = LoggerFactory.getLogger(AddController.class);


    @Autowired
    private ProjectService projectService;
    @Autowired
    private SiteService siteService;
    @Autowired
    public BrigadeService brigadeService;
    @Autowired
    public CategoryService categoryService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private EstimateService estimateService;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private BuildingManagementService buildingManagementService;
    @Autowired
    private JobCategoryService jobCategoryService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/add_info_in_project")
    public String saveInfo(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "siteId", required = false) Integer siteId,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model,
            HttpSession session
    ) throws IOException, ParseException {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);


        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Конвертація строк у дати
        LocalDate parsedStartDate = null;
        LocalDate parsedEndDate = null;

        try {
            if (startDate != null && !startDate.isEmpty()) {
                parsedStartDate = LocalDate.parse(startDate, dateFormat); // Перетворюємо строку на LocalDate
            }
            if (endDate != null && !endDate.isEmpty()) {
                parsedEndDate = LocalDate.parse(endDate, dateFormat); // Перетворюємо строку на LocalDate
            }
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format", e);
            model.addAttribute("error", "Invalid date format. Please use yyyy-MM-dd.");
            return "add_info";
        }

        projectService.saveProject(name, categoryId, siteId, parsedStartDate, parsedEndDate, image);

        return "add_info";
    }

    @PostMapping("/add_info_in_site")
    public String saveInfoInSite(
            @RequestParam(value = "name_site", required = false) String name,
            @RequestParam(value = "management_id", required = false) int managementId,
            @RequestParam(value = "location", required = false) String location,
            Model model,
            HttpSession session
    )
        throws IOException, ParseException {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        logger.info("User input - Name: {}, Brigade: {}, location: {}", name, managementId, location);
        siteService.saveSite(name, managementId, location);

        return "add_info";
    }

    @PostMapping("/add_info_in_brigade")
    public String saveInfoInBrigade(
            @RequestParam(value = "name_brigade", required = false) String name,
            @RequestParam(value = "site_id", required = false) Integer siteId,
            @RequestParam(value = "leader_id", required = false) Integer leaderId,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");

        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));

        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) :
                new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);


        Brigade brigade = new Brigade();

        if (allowedFields.isEmpty() || Objects.equals(currentUser.getPosition(), "адміністратор")) {
            brigade.setName(name);
            brigade.setSiteId(siteId);
            brigade.setLeaderId(leaderId);
            brigadeService.saveBrigade(brigade);
        } else {
            // Якщо є дозволені поля, додаємо їх в об'єкт Brigade
            if (allowedFields.contains("name") && name != null && !name.isEmpty()) {
                brigade.setName(name);  // Оновлюємо поле, якщо воно дозволене
            } else if (!allowedFields.contains("name")) {
                brigade.setName(null);  // Якщо не дозволене — встановлюємо в null
            }

            if (allowedFields.contains("siteId") && siteId != null && siteId > 0) {
                brigade.setSiteId(siteId);  // Оновлюємо поле, якщо воно дозволене
            } else if (!allowedFields.contains("siteId")) {
                brigade.setSite(null);  // Якщо не дозволене — встановлюємо в null
            }

            if (allowedFields.contains("leaderId") && leaderId != null && leaderId > 0) {
                brigade.setLeaderId(leaderId);  // Оновлюємо поле, якщо воно дозволене
            } else if (!allowedFields.contains("leaderId")) {
                brigade.setLeaderId(null);  // Якщо не дозволене — встановлюємо в null
            }

            // Збереження об'єкта Brigade в базі даних
            brigadeService.saveBrigade(brigade);
        }

        return "add_info";
    }



    @PostMapping("/add_info_in_category")
    public String saveInfoInCategory(
            @RequestParam(value = "name_category", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        logger.info("User input - Name: {}, Description: {}", name, description);
        categoryService.saveCategory(name, description);

        return "add_info";
    }

    @PostMapping("/add_info_in_equipment")
    public String saveInfoInEquipment(
            @RequestParam(value = "name_equipment", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "site_id", required = false) int siteId,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        logger.info("User input - Name: {}, Type: {}, SiteId: {}", name, type, siteId);
        equipmentService.saveEquipment(name, type, siteId);

        return "add_info";
    }

    @PostMapping("/add_info_in_estimate")
    public String saveInfoInEstimate(
            @RequestParam(value = "project_id", required = false) int projectId,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam(value = "quantity", required = false) int quantity,
            @RequestParam(value = "cost", required = false) double cost,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        logger.info("User input - ProjectId: {}, Material: {}, Quantity: {}, Cost: {}", projectId, material, quantity, cost);
        estimateService.saveEstimate(projectId, material, quantity, cost);

        return "add_info";
    }

    @PostMapping("/add_info_in_report")
    public String saveInfoInReport(
            @RequestParam(value = "project_id", required = false) int projectId,
            @RequestParam(value = "work_type_id", required = false) int workTypeId,
            @RequestParam(value = "completion_date", required = false) String completionDateStr,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam(value = "used_material", required = false) int usedMaterial,
            @RequestParam(value = "actual_cost", required = false) double actualCost,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate completionDate = LocalDate.parse(completionDateStr, dateFormat);


            reportService.saveReport(projectId, workTypeId, completionDate, material, usedMaterial, actualCost);

            return "add_info";
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", completionDateStr, e);
            return "error";
        }
    }

    @PostMapping("/add_info_in_schedule")
    public String saveInfoInSchedule(
            @RequestParam(value = "project_id", required = false) int projectId,
            @RequestParam(value = "work_type_id", required = false) int workTypeId,
            @RequestParam(value = "start_date", required = false) String startDateStr,
            @RequestParam(value = "end_date", required = false) String endDateStr,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startDate = LocalDate.parse(startDateStr, formatter);
        LocalDate endDate = LocalDate.parse(endDateStr, formatter);

        logger.info("User input - ProjectId: {}, WorkTypeId: {}, StartDate: {}, EndDate: {}",
                projectId, workTypeId, startDate, endDate);

        scheduleService.saveSchedule(projectId, workTypeId, startDate, endDate);

        return "add_info";
    }

    @PostMapping("/add_info_in_work_type")
    public String saveInfoInWorkType(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        workTypeService.saveWorkType(name, description);
        return "add_info";
    }

    @PostMapping("/add_info_in_building_managment")
    public String saveInfoInBuldingManagment(
            @RequestParam(value = "name", required = false) String name,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        buildingManagementService.saveBuildingManagement(name);
        return "add_info";
    }

    @PostMapping("/add_info_in_job_category")
    public String saveJobCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            Model model,
            HttpSession session
    ) {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        jobCategoryService.saveJobCategory(name, description);
        return "add_info";
    }

    @PostMapping("/add_info_in_employee")
    public String saveEmployee(
            @RequestParam("name") String name,
            @RequestParam("position") String position,
            @RequestParam("jobCategory.id") int jobCategoryId,
            @RequestParam("site.id") int siteId,
            @RequestParam("brigade.id") int brigadeId,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Model model,
            HttpSession session
    ) throws IOException {
        keysService.setUserRoles(model, session);
        Keys currentUser = (Keys) session.getAttribute("user");
        model.addAttribute("allowedTables", keysService.getAvailableTables(currentUser));
        List<String> allowedFields = currentUser.getAllowedFields() != null ?
                Arrays.asList(currentUser.getAllowedFields().split(",")) : new ArrayList<>();
        model.addAttribute("allowedFields", allowedFields);
        employeeService.saveEmployee(name, position, jobCategoryId, siteId, brigadeId, image);

        return "redirect:/employees";
    }
}
