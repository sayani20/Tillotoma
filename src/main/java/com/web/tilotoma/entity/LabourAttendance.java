package com.web.tilotoma.entity;
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
    private Labour labour;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    private LocalTime inTime;
    private LocalTime outTime;

    @Column(name = "is_present")
    private Boolean isPresent = true;
}
