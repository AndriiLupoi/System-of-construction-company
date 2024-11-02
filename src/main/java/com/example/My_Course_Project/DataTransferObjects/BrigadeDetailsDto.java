package com.example.My_Course_Project.DataTransferObjects;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class BrigadeDetailsDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String workTypeName;
    private String siteName;

    public BrigadeDetailsDto(String name, LocalDate startDate, LocalDate endDate, String workTypeName, String siteName) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.workTypeName = workTypeName;
        this.siteName = siteName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}