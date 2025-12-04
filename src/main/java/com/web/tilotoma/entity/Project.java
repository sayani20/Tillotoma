package com.web.tilotoma.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @Column(nullable = true)
    private ProjectType projectType; // STAND_ALONE or APARTMENT

    @Column(nullable = true)
    private String projectStatus; // ONGOING, COMPLETED, UPCOMING

    @Column(nullable = true)
    private Integer totalNumberOfFlats;

    @Column(nullable = true)
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    @JsonBackReference
    private Contractor contractor;

    /*@ManyToMany
    @JoinTable(
            name = "contractor_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "contractor_id")
    )
    private List<Contractor> contractors;*/

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("projects")
    private List<Labour> labours;

}


