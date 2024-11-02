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
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    // Додайте це поле
    @Column(name = "site_id")
    private int siteId;

    // Додайте це поле
    @Column(name = "leader_id")
    private int leaderId;

    @ManyToOne
    @JoinColumn(name = "site_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Site site; // Припустимо, що у вас є клас Site

    // Додайте зв'язки з іншими таблицями, якщо потрібно
    @OneToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Employee leader; // Припустимо, що у вас є клас Employee
}
