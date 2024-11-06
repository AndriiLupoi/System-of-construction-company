package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.DataTransferObjects.ProjectDTOs.ProjectDTO;
import com.example.My_Course_Project.model.JobCategory;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.My_Course_Project.DataTransferObjects.SiteDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Конвертація строк у дати
        Date parsedStartDate = null;
        Date parsedEndDate = null;

        if (startDate != null && !startDate.isEmpty()) {
            parsedStartDate = dateFormat.parse(startDate); // Конвертуємо строку у об'єкт Date
        }
        if (endDate != null && !endDate.isEmpty()) {
            parsedEndDate = dateFormat.parse(endDate);
        }

        logger.info("User input - Name: {}, CategoryId: {}, SiteId: {}", name, categoryId, siteId);

        logger.info("Saving new project: Name = {}, Start Date = {}, End Date = {}, Image size = {}",
                name, startDate, endDate, image != null ? image.getSize() : "No Image");

        // Передаємо вже конвертовані дати
        projectService.saveProject(name, categoryId, siteId, parsedStartDate, parsedEndDate, image);
        logger.info("Project successfully saved.");

        return "add_info";
    }

    @PostMapping("/add_info_in_site")
    public String saveInfoInSite(
            @RequestParam(value = "name_site", required = false) String name,
            @RequestParam(value = "management_id", required = false) int managementId,
            @RequestParam(value = "location", required = false) String location
    )
        throws IOException, ParseException {

        logger.info("User input - Name: {}, Brigade: {}, location: {}", name, managementId, location);
        siteService.saveSite(name, managementId, location);

        return "add_info";
    }

    @PostMapping("/add_info_in_brigade")
    public String saveInfoInBrigade(
            @RequestParam(value = "name_brigade", required = false) String name,
            @RequestParam(value = "site_id", required = false) int siteId,
            @RequestParam(value = "leader_id", required = false) int leaderId
    ) {
        logger.info("User input - Name: {}, SiteId: {}, LeaderId: {}", name, siteId, leaderId);
        brigadeService.saveBrigade(name, siteId, leaderId);

        return "add_info";
    }

    @PostMapping("/add_info_in_category")
    public String saveInfoInCategory(
            @RequestParam(value = "name_category", required = false) String name,
            @RequestParam(value = "description", required = false) String description
    ) {
        logger.info("User input - Name: {}, Description: {}", name, description);
        categoryService.saveCategory(name, description);

        return "add_info";
    }

    @PostMapping("/add_info_in_equipment")
    public String saveInfoInEquipment(
            @RequestParam(value = "name_equipment", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "site_id", required = false) int siteId
    ) {
        logger.info("User input - Name: {}, Type: {}, SiteId: {}", name, type, siteId);
        equipmentService.saveEquipment(name, type, siteId);

        return "add_info";
    }

    @PostMapping("/add_info_in_estimate")
    public String saveInfoInEstimate(
            @RequestParam(value = "project_id", required = false) int projectId,
            @RequestParam(value = "material", required = false) String material,
            @RequestParam(value = "quantity", required = false) int quantity,
            @RequestParam(value = "cost", required = false) double cost
    ) {
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
            @RequestParam(value = "actual_cost", required = false) double actualCost
    ) {
        try {
            // Перетворення рядка дати у java.util.Date
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate completionDate = LocalDate.parse(completionDateStr, dateFormat);


            reportService.saveReport(projectId, workTypeId, completionDate, material, usedMaterial, actualCost);

            return "add_info"; // Повернення до потрібної сторінки
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format: {}", completionDateStr, e);
            return "error"; // Вказівка на помилку
        }
    }

    @PostMapping("/add_info_in_schedule")
    public String saveInfoInSchedule(
            @RequestParam(value = "project_id", required = false) int projectId,
            @RequestParam(value = "work_type_id", required = false) int workTypeId,
            @RequestParam(value = "start_date", required = false) String startDateStr,
            @RequestParam(value = "end_date", required = false) String endDateStr
    ) {
        // Формат дати
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Перетворення рядків у LocalDate
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
            @RequestParam(value = "description", required = false) String description
    ) {
        workTypeService.saveWorkType(name, description);
        return "add_info"; // Повернення до потрібної сторінки
    }

    @PostMapping("/add_info_in_building_managment")
    public String saveInfoInBuldingManagment(
            @RequestParam(value = "name", required = false) String name
    ) {
        buildingManagementService.saveBuildingManagement(name);
        return "add_info";
    }

    @PostMapping("/add_info_in_job_category")
    public String saveJobCategory(
            @RequestParam("name") String name,
            @RequestParam("description") String description
    ) {
        jobCategoryService.saveJobCategory(name, description);
        return "add_info";
    }
}
