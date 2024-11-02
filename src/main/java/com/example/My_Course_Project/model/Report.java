package com.example.My_Course_Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "project_id")
    private Integer projectId;

    @Column(name = "work_type_id")
    private int workTypeId;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "material")
    private String material;

    @Column(name = "used")
    private int usedMaterial;

    @Column(name = "actual_cost")
    private double actualCost;
}
