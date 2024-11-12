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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            return "redirect:/login";
        }

        userService.setUserRoles(model, session);

        Keys currentUser = (Keys) session.getAttribute("user");

        Map<String, List<String>> tableColumns = getTableColumns();

        model.addAttribute("tables", userService.getAvailableTables(currentUser));

        boolean showFields = "оператор".equals(position) && tableColumns.containsKey(allowedTables);
        model.addAttribute("showFields", showFields);

        if (showFields) {
            model.addAttribute("fields", tableColumns.get(allowedTables));
        }
        String allowedFieldsStr = (allowedFields != null && !allowedFields.isEmpty()) ? String.join(",", allowedFields) : "";
        user.setAllowedFields(allowedFieldsStr);

        if (allowedTables != null && position != null && !allowedFieldsStr.isEmpty()) {
            userService.saveUser(user);
            return "redirect:/home";
        } else if (allowedTables != null && position != null && (allowedFields == null || allowedFields.isEmpty())) {
            userService.saveUser(user);
            return "redirect:/home";
        }


        model.addAttribute("user", user);
        model.addAttribute("allowedTables", allowedTables);
        model.addAttribute("allowedFields", allowedFields);

        return "userAdd";
    }


    public Map<String, List<String>> getTableColumns() {
        Map<String, List<String>> tableColumns = new HashMap<>();
        tableColumns.put("building_management", List.of("name"));
        tableColumns.put("category", List.of("name", "description"));
        tableColumns.put("equipment", List.of("name", "type", "site_id"));
        tableColumns.put("estimate", List.of("project_id", "material", "quantity", "cost"));
        tableColumns.put("job_category", List.of("name", "description"));
        tableColumns.put("project", List.of("name", "category_id", "site_id", "start_date", "end_date", "image"));
        tableColumns.put("report", List.of("project_id", "work_type_id", "completion_date", "material", "used", "actual_cost"));
        tableColumns.put("schedule", List.of("project_id", "work_type_id", "start_date", "end_date", "brigade_id", "site_id"));
        tableColumns.put("site", List.of("name", "management_id", "location"));
        tableColumns.put("work_type", List.of("name", "description"));
        return tableColumns;
    }

}
