package com.example.My_Course_Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "work_type_id", referencedColumnName = "id")
    private WorkType workType;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "material")
    private String material;

    @Column(name = "used")
    private Integer usedMaterial;

    @Column(name = "actual_cost")
    private Double actualCost;
}
