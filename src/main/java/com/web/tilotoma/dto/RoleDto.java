package com.web.tilotoma.dto;

import java.time.LocalDateTime;

public class RoleDto {
    private Long id;
    private String name;
    private boolean isActive;
    private LocalDateTime createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public RoleDto(String name, Long id, boolean isActive, LocalDateTime createdOn) {
        this.name = name;
        this.id = id;
        this.isActive = isActive;
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "RoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", createdOn=" + createdOn +
                '}';
    }
}
