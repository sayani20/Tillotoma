package com.web.tilotoma.entity;

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
    private Contractor contractor;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "labour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabourAttendance> attendanceRecords;
}
