package com.web.tilotoma.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "contractors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contractorName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15)
    private String mobileNumber;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    /**
     * Contractor kon kon LabourType provide kore.
     */
    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractorLabourRate> labourRates;

    @OneToMany(mappedBy = "contractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Labour> labours;

    @ManyToMany
    @JoinTable(
            name = "contractor_projects",
            joinColumns = @JoinColumn(name = "contractor_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;
}
