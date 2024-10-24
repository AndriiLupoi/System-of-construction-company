package com.example.My_Course_Project.DataTransferObjects;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

public class SiteDTO {

    @Setter
    @Getter
    private int id;
    @NotNull(message = "Назва не може бути порожньою")
    @Size(min = 1, message = "Назва повинна містити хоча б один символ")
    private String name;

    @NotNull(message = "ID управління не може бути порожнім")
    private Integer managementId;

    @NotNull(message = "Розташування не може бути порожнім")
    @Size(min = 1, message = "Розташування повинно містити хоча б один символ")
    private String location;

    public SiteDTO(int id, String name, Integer managementId, String location) {
        this.id = id;
        this.name = name;
        this.managementId = managementId;
        this.location = location;
    }

    public @NotNull(message = "Назва не може бути порожньою") @Size(min = 1, message = "Назва повинна містити хоча б один символ") String getName() {
        return name;
    }

    public void setName(@NotNull(message = "Назва не може бути порожньою") @Size(min = 1, message = "Назва повинна містити хоча б один символ") String name) {
        this.name = name;
    }

    public @NotNull(message = "ID управління не може бути порожнім") Integer getManagementId() {
        return managementId;
    }

    public void setManagementId(@NotNull(message = "ID управління не може бути порожнім") Integer managementId) {
        this.managementId = managementId;
    }

    public @NotNull(message = "Розташування не може бути порожнім") @Size(min = 1, message = "Розташування повинно містити хоча б один символ") String getLocation() {
        return location;
    }

    public void setLocation(@NotNull(message = "Розташування не може бути порожнім") @Size(min = 1, message = "Розташування повинно містити хоча б один символ") String location) {
        this.location = location;
    }
}
