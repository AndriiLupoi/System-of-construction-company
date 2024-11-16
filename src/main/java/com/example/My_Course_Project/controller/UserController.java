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

        model.addAttribute("tables", userService.getAvailableTables(currentUser));

        boolean showFields = "оператор".equals(position) && "brigade".equals(allowedTables);
        model.addAttribute("showFields", showFields);

        if (showFields) {
            model.addAttribute("fields", List.of("name", "siteId", "leaderId"));
        }

        String allowedFieldsStr = (allowedFields != null && !allowedFields.isEmpty()) ? String.join(",", allowedFields) : "";
        user.setAllowedFields(allowedFieldsStr);

        if (allowedTables != null && position != null && !allowedFieldsStr.isEmpty()) {
            userService.saveUser(user);
            return "redirect:/home";
        } else if (allowedTables != null && ("адміністратор".equals(position) || "користувач".equals(position))) {
            userService.saveUser(user);
            return "redirect:/home";

        }

        model.addAttribute("user", user);
        model.addAttribute("allowedTables", allowedTables);
        model.addAttribute("allowedFields", allowedFields);

        return "userAdd";
    }

}
