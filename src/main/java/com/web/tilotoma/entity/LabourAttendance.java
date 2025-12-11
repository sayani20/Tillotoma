package com.web.tilotoma.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "labour_attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabourAttendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labour_id", nullable = false)
    @JsonBackReference
    private Labour labour;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    private LocalTime inTime;
    private LocalTime outTime;

    @Column(name = "is_present")
    private Boolean isPresent = true;

    @Column(name = "is_check")
    private Boolean isCheck = false;


    // NEW: store system computed amount (immutable after compute unless recompute)
    @Column(name = "calculated_amount")
    private Double calculatedAmount;

    // NEW: admin-editable amount (initially same as calculatedAmount)
    @Column(name = "custom_amount")
    private Double customAmount;

    private Boolean isCheckManuallyUpdated = false;

    private Boolean isCustomUpdated = false;

}
