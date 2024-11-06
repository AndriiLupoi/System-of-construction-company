package com.example.My_Course_Project.repository;

import com.example.My_Course_Project.model.Keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeysRepository extends JpaRepository<Keys, Integer> {
    Keys findByLoginAndPassword(String login, String password);
    Keys findByLoginAndEmail(String login, String email);
    Keys findByLogin(String login);
}
