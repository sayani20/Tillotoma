package com.web.tilotoma.entity.material;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "vendors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Basic Info
    @Column(nullable = false)
    private String vendorName;

    private String contactPersonName;

    @Column(length = 15)
    private String mobile;

    @Column(length = 100)
    private String emailId;

    @Column(length = 20)
    private String gstNumber;

    @Column(length = 255)
    private String address;

    // Status
    @Column(nullable = false)
    private Boolean isActive = true;
}

