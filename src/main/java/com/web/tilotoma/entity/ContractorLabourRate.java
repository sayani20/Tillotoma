package com.web.tilotoma.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contractor_labour_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractorLabourRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "labour_type_id", nullable = false)
    private LabourType labourType;

    private Double dailyRate;
    private Double hourlyRate;
}
