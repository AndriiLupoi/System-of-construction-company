package com.example.My_Course_Project.model;

import jakarta.persistence.*; // Імпорт всіх анотацій JPA


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity
@Table(name = "ukeys")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Keys {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "position")
    private String position;

    @Column(name = "email")
    private String email;

    @Column(name = "verification_сode")
    private String verificationCode;

    @Column(name = "allowed_tables")
    private String allowedTables;

}
