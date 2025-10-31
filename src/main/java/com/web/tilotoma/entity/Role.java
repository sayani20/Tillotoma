package com.web.tilotoma.entity;



import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    @Column(nullable = false, unique = true)
    private String name;

    private boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

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

    public Role(Long id, String name, boolean isActive, LocalDateTime createdOn) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isActive=" + isActive +
                ", createdOn=" + createdOn +
                '}';
    }
}


