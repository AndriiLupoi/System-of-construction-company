package com.example.My_Course_Project.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private String position;

    @OneToOne
    @JoinColumn(name = "site_id", referencedColumnName = "id")
    private Site site;

    @ManyToOne
    @JoinColumn(name = "brigade_id", referencedColumnName = "id")
    private Brigade brigade;

    @Column(name = "image")
    private byte[] image;
}
