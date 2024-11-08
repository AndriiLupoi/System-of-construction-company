package com.example.My_Course_Project.controller;

import com.example.My_Course_Project.DataTransferObjects.ProjectDTOs.ProjectDTO;
import com.example.My_Course_Project.model.*;
import com.example.My_Course_Project.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
@Controller
public class DataController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private BuildingManagementService buildingManagementService;

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SiteService siteService;

    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private JobCategoryService jobCategoryService;
    @Autowired
    private KeysService keysService;


    @GetMapping("/data")
    public String viewTable(@RequestParam("tableName") String tableName, Model model, HttpSession session) {
        // Перевіряємо, чи є користувач в сесії
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін
        }

        keysService.setUserRoles(model, session);

        switch (tableName) {
            case "project":
                List<ProjectDTO> projects = projectService.getAllProjectsWithoutImages();
                model.addAttribute("data", projects);
                model.addAttribute("tableName", "project");
                break;

            case "equipment":
                List<Equipment> equipment = equipmentService.getAllEquipment();
                model.addAttribute("data", equipment);
                model.addAttribute("tableName", "equipment");
                break;

            case "category":
                List<Category> categories = categoryService.getAllCategories(); // Потрібен сервіс для категорій
                model.addAttribute("data", categories);
                model.addAttribute("tableName", "category");
                break;

            case "brigade":
                List<Brigade> brigades = brigadeService.getAllBrigades(); // Потрібен сервіс для бригад
                model.addAttribute("data", brigades);
                model.addAttribute("tableName", "brigade");
                break;

            case "building_management":
                List<BuildingManagement> buildings = buildingManagementService.getAllBuildings(); // Потрібен сервіс для управлінь будівництвом
                model.addAttribute("data", buildings);
                model.addAttribute("tableName", "building_management");
                break;

            case "estimate":
                List<Estimate> estimates = estimateService.getAllEstimates(); // Потрібен сервіс для кошторисів
                model.addAttribute("data", estimates);
                model.addAttribute("tableName", "estimate");
                break;

            case "report":
                List<Report> reports = reportService.getAllReports(); // Потрібен сервіс для звітів
                model.addAttribute("data", reports);
                model.addAttribute("tableName", "report");
                break;

            case "schedule":
                List<Schedule> schedules = scheduleService.getAllSchedules(); // Потрібен сервіс для розкладів
                model.addAttribute("data", schedules);
                model.addAttribute("tableName", "schedule");
                break;

            case "site":
                List<Site> sites = siteService.getAllSites(); // Потрібен сервіс для сайтів
                model.addAttribute("data", sites);
                model.addAttribute("tableName", "site");
                break;

            case "work_type":
                List<WorkType> workTypes = workTypeService.getAllWorkTypes(); // Потрібен сервіс для типів робіт
                model.addAttribute("data", workTypes);
                model.addAttribute("tableName", "work_type");
                break;

            case "jobCategory":
                List<JobCategory> jobCategories = jobCategoryService.getAllJobCategory();
                model.addAttribute("jobCategories", jobCategories);
                model.addAttribute("tableName", "jobCategory");
                break;
            default:
                model.addAttribute("error", "Таблиця не знайдена");
                return "error";
        }
        return "tables"; // Повертаємо шаблон таблиць з даними
    }


    @GetMapping("/data/search")
    public String searchProjects(@RequestParam("tableName") String tableName,
                                 @RequestParam("query") String query,
                                 Model model,
                                 HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // Якщо користувач не аутентифікований, перенаправляємо на логін

        }
        keysService.setUserRoles(model, session);

        switch (tableName){
            case "project":
                List<Project> projects = projectService.searchProjects(query);
                model.addAttribute("data", projects);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "brigade":
                List<Brigade> brigades = brigadeService.searchBrigades(query);
                model.addAttribute("data", brigades);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "building_management":
                List<BuildingManagement> buildingManagements = buildingManagementService.searchBuildingManagements(query);
                model.addAttribute("data", buildingManagements);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "category":
                List<Category> categories = categoryService.searchCategories(query);
                model.addAttribute("data", categories);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "equipment":
                List<Equipment> equipment = equipmentService.searchEquipment(query);
                model.addAttribute("data", equipment);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "estimate":
                List<Estimate> estimates = estimateService.searchEstimates(query);
                model.addAttribute("data", estimates);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "report":
                List<Report> reports = reportService.searchReports(query);
                model.addAttribute("data", reports);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "schedule":
                List<Schedule> schedules = scheduleService.searchSchedules(query);
                model.addAttribute("data", schedules);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "site":
                List<Site> sites = siteService.searchSites(query);
                model.addAttribute("data", sites);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "work_type":
                List<WorkType> workTypes = workTypeService.searchWorkTypes(query);
                model.addAttribute("data", workTypes);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;
            case "jobCategory":
                List<JobCategory> jobCategories = jobCategoryService.searchJobCategories(query);
                model.addAttribute("jobCategories", jobCategories);

                model.addAttribute("tableName", tableName);
                model.addAttribute("query", query);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + tableName);
        }

        return "tables";
    }

    @PostMapping("/data/delete/{id}")
    public String deleteRecord(@RequestParam("tableName") String tableName, @PathVariable("id") int id) {
        switch (tableName) {
            case "project":
                projectService.deleteProjectById(id);
                break;
            case "equipment":
                equipmentService.deleteEquipmentById(id);
                break;
            case "category":
                categoryService.deleteCategoryById(id);
                break;
            case "brigade":
                brigadeService.deleteBrigadeById(id);
                break;
            case "building_management":
                buildingManagementService.deleteBuildingManagementById(id);
                break;
            case "estimate":
                estimateService.deleteEstimateById(id);
                break;
            case "report":
                reportService.deleteReportById(id);
                break;
            case "schedule":
                scheduleService.deleteScheduleById(id);
                break;
            case "site":
                siteService.deleteSiteById(id);
                break;
            case "work_type":
                workTypeService.deleteWorkTypeById(id);
                break;
            case "jobCategory":
                jobCategoryService.deleteJobCategoryById(id);
                break;

            default:
                throw new IllegalArgumentException("Неправильна назва таблиці: " + tableName);
        }
        return "redirect:/data?tableName=" + tableName;
    }

    @GetMapping("/data/edit/{tableName}/{id}")
    public String showEditForm(@PathVariable("tableName") String tableName, @PathVariable("id") int id, Model model) {
        switch (tableName) {
            case "project":
                Project project = projectService.findProjectById(id);
                model.addAttribute("projects", project);
                break;
            case "equipment":
                Equipment equipment = equipmentService.findEquipmentById(id);
                List<Site> sites = siteService.getAllSites();
                model.addAttribute("equipment", equipment);
                model.addAttribute("sites", sites);
                break;
            case "category":
                Category category = categoryService.findCategoryById(id);  // Пошук категорії
                model.addAttribute("category", category);  // Додавання категорії в модель
                break;
            case "brigade":
                Brigade brigade = brigadeService.findBrigadeById(id);
                List<Site> siteInBrigade = siteService.getAllSites();
                model.addAttribute("brigade", brigade);
                model.addAttribute("siteInBrigade", siteInBrigade);
                break;
            case "building_management":
                BuildingManagement building = buildingManagementService.findBuildingById(id); // Отримуємо об'єкт будівлі
                model.addAttribute("building", building);
                break;
            case "estimate":
                Estimate estimate = estimateService.findEstimateById(id);  // Пошук об'єкта estimate
                model.addAttribute("estimate", estimate);
                break;
            case "report":
                Report report = reportService.findReportById(id);  // Пошук об'єкта report
                model.addAttribute("report", report);
                break;
            case "schedule":
                Schedule schedule = scheduleService.findScheduleById(id);
                model.addAttribute("schedule", schedule);
                break;
            case "site":
                Site site = siteService.findSiteById(id);
                model.addAttribute("site", site);
                break;
            case "work_type":
                WorkType workType = workTypeService.findWorkTypeById(id);
                model.addAttribute("workType", workType);
                break;
            case "jobCategory":
                JobCategory jobCategory = jobCategoryService.findJobCategoryById(id);
                model.addAttribute("jobCategory", jobCategory);
                break;
            default:
                throw new IllegalArgumentException("Неправильна назва таблиці: " + tableName);
        }
        model.addAttribute("tableName", tableName);

        // Вказуємо шаблон для редагування
        return "editForm";
    }

    @PostMapping("/data/edit/{tableName}/{id}")
    public String updateEntity(@PathVariable("tableName") String tableName,
                               @PathVariable("id") int id,
                               @RequestParam(required = false) String startDate,
                               @RequestParam(required = false) String endDate,
                               @RequestParam(required = false) String name,
                               @RequestParam(required = false) String type,
                               @RequestParam(value = "site_id", required = false) Integer siteId,
                               @RequestParam(required = false) String description,
                               @RequestParam(value = "leader_id", required = false) String leaderId,
                               @RequestParam(required = false) Integer buildingManagementId,
                               @RequestParam(required = false) String location,
                               @RequestParam(required = false) Integer projectId,
                               @RequestParam(required = false) String material,
                               @RequestParam(required = false) Integer quantity,
                               @RequestParam(required = false) Double cost,
                               @RequestParam(required = false) Integer workTypeId,
                               @RequestParam(required = false) String completionDate,
                               @RequestParam(required = false) String usedMaterial,
                               @RequestParam(required = false) Double actualCost,
                               @RequestParam(value = "brigade_id", required = false) Integer brigadeId,
                               @ModelAttribute("projects") Project project,
                               @ModelAttribute("equipment") Equipment equipment) {

        switch (tableName) {
            case "project":
                try {
                    if (startDate != null && !startDate.isEmpty()) {
                        LocalDate parsedStartDate = LocalDate.parse(startDate);
                        project.setStartDate(parsedStartDate);
                    }
                    if (endDate != null && !endDate.isEmpty()) {
                        LocalDate parsedEndDate = LocalDate.parse(endDate);
                        project.setEndDate(parsedEndDate);
                    }
                    projectService.updateProjectById(id, project);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                break;
            case "equipment":
                if (name != null && !name.isEmpty()) {
                    equipment.setName(name);
                }
                if (type != null && !type.isEmpty()) {
                    equipment.setType(type);
                }
                if (siteId > 0) {
                    Site site = siteService.findSiteById(siteId);
                    equipment.setSite(site);
                }
                equipmentService.updateEquipmentById(id, equipment);
                break;
            case "category":
                Category category = categoryService.findCategoryById(id);
                category.setName(name);
                category.setDescription(description);
                categoryService.updateCategoryById(id, category);
                break;
            case "brigade":
                Brigade brigade = brigadeService.findBrigadeById(id);
                if (name != null && !name.isEmpty()) {
                    brigade.setName(name);
                }
                if (siteId != null) {
                    brigade.setSiteId(siteId);
                }
                if (leaderId != null) {
                    brigade.setLeaderId(Integer.parseInt(leaderId));
                }
                brigadeService.updateBrigadeById(id, brigade);
                break;
            case "building_management":
                BuildingManagement building = buildingManagementService.findBuildingById(id);
                if (name != null && !name.isEmpty()) {
                    building.setName(name);
                }
                buildingManagementService.updateBuildingById(id, building);
                break;
            case "estimate":
                Estimate estimate = estimateService.findEstimateById(id);
                if (projectId != null) {
                    estimate.setProjectId(projectId);
                }
                if (material != null && !material.isEmpty()) {
                    estimate.setMaterial(material);
                }
                if (quantity != null) {
                    estimate.setQuantity(quantity);
                }
                if (cost != null) {
                    estimate.setCost(cost);
                }
                estimateService.updateEstimateById(id, estimate);
                break;
            case "report":
                Report report = reportService.findReportById(id);
                if (projectId != null) {
                    Project projectReport = projectService.findProjectById(projectId);
                    report.setProject(projectReport);
                }
                if (workTypeId != null) {
                    WorkType workType = workTypeService.findWorkTypeById(workTypeId);
                    report.setWorkType(workType);
                }
                if (completionDate != null && !completionDate.isEmpty()) {
                    report.setCompletionDate(LocalDate.parse(completionDate));
                }
                if (material != null && !material.isEmpty()) {
                    report.setMaterial(material);
                }
                if (usedMaterial != null && !usedMaterial.isEmpty()) {
                    report.setUsedMaterial(Integer.parseInt(usedMaterial));
                }
                if (actualCost != null) {
                    report.setActualCost(actualCost);
                }
                reportService.updateReportById(id, report);
                break;
            case "schedule":
                Schedule schedule = scheduleService.findScheduleById(id);
                if (startDate != null && !startDate.isEmpty()) {
                    schedule.setStartDate(LocalDate.parse(startDate));
                }
                if (endDate != null && !endDate.isEmpty()) {
                    schedule.setEndDate(LocalDate.parse(endDate));
                }
                if (projectId != null) {
                    Project projectSchedule = projectService.findProjectById(projectId);
                    schedule.setProject(projectSchedule);
                }
                if (workTypeId != null) {
                    WorkType workType = workTypeService.findWorkTypeById(workTypeId);
                    schedule.setWorkType(workType);
                }
                if (brigadeId != null) {
                    Brigade brigadeSchedule = brigadeService.findBrigadeById(brigadeId);
                    schedule.setBrigade(brigadeSchedule);
                }
                if (siteId != null) {
                    Site site = siteService.findSiteById(siteId);
                    schedule.setSite(site);
                }
                scheduleService.updateScheduleById(id, schedule);
                break;
            case "site":
                Site site = siteService.findSiteById(id);
                if (name != null) {
                    site.setName(name);
                }
                if (buildingManagementId != null) {
                    BuildingManagement buildingManagement = buildingManagementService.findBuildingById(buildingManagementId);
                    site.setBuildingManagement(buildingManagement);
                }
                if (location != null) {
                    site.setLocation(location);
                }
                siteService.updateSiteById(id, site);
                break;
            case "work_type":
                WorkType workType = workTypeService.findWorkTypeById(id);
                if (name != null && !name.isEmpty()) {
                    workType.setName(name);
                }
                if (description != null && !description.isEmpty()) {
                    workType.setDescription(description);
                }
                workTypeService.updateWorkTypeById(id, workType);
                break;
            case "jobCategory":
                JobCategory jobCategory = jobCategoryService.findJobCategoryById(id);
                if (name != null && !name.isEmpty()) {
                    jobCategory.setName(name);
                }
                if (description != null && !description.isEmpty()) {
                    jobCategory.setDescription(description);
                }
                jobCategoryService.updateJobCategoryById(id, jobCategory);
                break;

            default:
                throw new IllegalArgumentException("Неправильна назва таблиці: " + tableName);
        }

        return "redirect:/data?tableName=" + tableName;
    }

}
