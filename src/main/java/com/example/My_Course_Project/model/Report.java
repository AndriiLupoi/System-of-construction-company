package com.example.My_Course_Project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private int projectId;

    @Column(name = "work_type_id")
    private int workTypeId;

    @Column(name = "completion_date")
    private Date completionDate;

    @Column(name = "actual_material_used")
    private String actualMaterialUsed;

    @Column(name = "actual_cost")
    private double actualCost;
}
