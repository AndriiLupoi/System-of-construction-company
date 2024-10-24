package com.example.My_Course_Project.DataTransferObjects.ProjectDTOs;

import java.util.Date;

public class ProjectDTO {
    private int id;
    private String name;
    private int categoryId;
    private int siteId;
    private Date startDate;
    private Date endDate;

    // Конструктори, геттери і сеттери
    public ProjectDTO(int id, String name, int categoryId, int siteId, Date startDate, Date endDate) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.siteId = siteId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Геттери та сеттери
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public int getSiteId() { return siteId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
}