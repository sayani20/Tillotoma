package com.web.tilotoma.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "labours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Labour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labourName;

    private String email;

    @Column(length = 15)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labour_type_id", nullable = false)
    private LabourType labourType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    @JsonBackReference
    private Contractor contractor;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "labour", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LabourAttendance> attendanceRecords;

    @ManyToMany
    @JoinTable(
            name = "labour_projects",
            joinColumns = @JoinColumn(name = "labour_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;

    @Column(name = "rate_per_hour")
    private Double ratePerHour;

    @Column(name = "rate_per_day")
    private Double ratePerDay;
}
