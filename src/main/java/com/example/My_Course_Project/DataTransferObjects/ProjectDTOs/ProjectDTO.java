package com.example.My_Course_Project.DataTransferObjects.ProjectDTOs;

import java.time.LocalDate;
import java.util.Date;

public class ProjectDTO {
    private int id;
    private String name;
    private int categoryId;
    private int siteId;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectDTO(int id, String name, int categoryId, int siteId, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.siteId = siteId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryId() { return categoryId; }
    public int getSiteId() { return siteId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}