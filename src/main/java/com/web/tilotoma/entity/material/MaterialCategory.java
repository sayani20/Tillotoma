package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, unique = true)
    private String name;

    @Column(name="is_active")
    private Boolean isActive = true;

    @Column(name="created_on")
    private LocalDateTime createdOn;
}

