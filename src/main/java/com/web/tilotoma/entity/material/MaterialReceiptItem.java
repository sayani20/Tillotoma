package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "material_receipt_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private MaterialReceipt receipt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private Double receivedQty;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private Double amount;
}

