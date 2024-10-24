package com.example.My_Course_Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Entity
@Table(name = "brigade")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Brigade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Додаємо генерацію ID
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "site_id")
    private Integer siteId;

    @Column(name = "leader_id")
    private Integer leaderId;
}
