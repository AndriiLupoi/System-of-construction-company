package com.example.My_Course_Project.service;

import com.example.My_Course_Project.model.Keys;
import com.example.My_Course_Project.repository.KeysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeysService {

    @Autowired
    private KeysRepository keysRepository;

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
}
