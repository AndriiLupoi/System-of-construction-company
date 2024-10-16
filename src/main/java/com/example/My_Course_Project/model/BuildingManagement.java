package com.example.My_Course_Project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;



@Entity
@Table(name = "brigade")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class BuildingManagement {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}
