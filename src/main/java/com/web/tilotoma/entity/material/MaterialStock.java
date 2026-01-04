package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "material_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialStock {

    @Id
    @OneToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(nullable = false)
    private Double availableQty;
}
