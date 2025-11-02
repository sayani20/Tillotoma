package com.web.tilotoma.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private String location;
    private String city;
    private String state;
    private String pincode;

    private Double landArea;
    private Double builtUpArea;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime possessionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectType projectType; // STAND_ALONE or APARTMENT

    @Column(nullable = false)
    private String projectStatus; // ONGOING, COMPLETED, UPCOMING

    @Column(nullable = false)
    private Integer totalNumberOfFlats;

    @Column(nullable = false)
    private Integer totalNumberOfFloors;

    private Double budgetEstimate;
    private String remarks;

    private Boolean approvedByAuthority = false;
    private String approvalNumber;

    @Column(nullable = false)
    private Long createdBy; // User ID of Super Admin

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    private Boolean isActive = true;

    @ManyToMany(mappedBy = "projects")
    private List<Contractor> contractors;
}


