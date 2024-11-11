package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.repository.KeysRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Service
public class KeysService {

    @Autowired
    private KeysRepository keysRepository;

    public List<Keys> getAllKeys() {
        return keysRepository.findAll();
    }

    public Keys checkLogin(String login, String password) {
        return keysRepository.findByLoginAndPassword(login, password);
    }

    public Keys findByLoginAndEmail(String login, String email) {
        return keysRepository.findByLoginAndEmail(login, email);
    }

    public boolean updatePassword(String login, String newPassword) {
        Keys user = keysRepository.findByLogin(login);
        if (user != null) {
            user.setPassword(newPassword); // Зашифруйте пароль за необхідністю
            keysRepository.save(user);
            return true;
        }
        return false;
    }

    public void saveVerificationCode(String login, String verificationCode) {
        Keys user = keysRepository.findByLogin(login);
        if (user != null) {
            user.setVerificationCode(verificationCode);
            keysRepository.save(user);
        }
    }

    public void setUserRoles(Model model, HttpSession session) {
        Keys currentUser = (Keys) session.getAttribute("user");

        boolean isAdmin = currentUser != null && "адміністратор".equalsIgnoreCase(currentUser.getPosition());
        boolean isOwner = currentUser != null && "власник".equalsIgnoreCase(currentUser.getPosition());
        boolean isOperator = currentUser != null && "оператор".equalsIgnoreCase(currentUser.getPosition());
        boolean isUser = currentUser != null && "користувач".equalsIgnoreCase(currentUser.getPosition());

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isOperator", isOperator);
        model.addAttribute("isUser", isUser);
    }

    public List<String> getAllowedTablesForUser(Keys currentUser) {
        String allowedTablesStr = currentUser.getAllowedTables(); // Отримуємо список дозволених таблиць
            return Arrays.asList(allowedTablesStr.split(","));
    }
}
