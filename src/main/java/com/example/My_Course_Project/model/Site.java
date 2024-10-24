package com.example.My_Course_Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "site")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Додаємо генерацію ID
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "management_id")
    private int managementId;

    @Column(name = "location")
    private String location;
}
