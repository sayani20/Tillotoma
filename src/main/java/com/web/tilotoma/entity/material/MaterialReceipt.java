package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "material_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private MaterialOrder order;

    @Column(nullable = false)
    private String receiptNo;

    @Column(nullable = false)
    private LocalDate receiptDate;

    private String createdBy;
}

