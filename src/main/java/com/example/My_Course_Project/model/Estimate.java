package com.example.My_Course_Project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estimate")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estimate {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "project_id")
    private int projectId;

    @Column(name = "material")
    private String material;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "cost")
    private double cost;
}
