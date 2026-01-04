package com.web.tilotoma.entity.labour;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(name = "mobile_number", length = 15)
        private String mobileNumber;

        @Column(name = "aadhar_number", unique = true, length = 12)
        private String aadharNumber;


        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "role_id", nullable = false)
        private Role role;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "created_by", nullable = true)
        private User createdBy;

        @Column(name = "is_active")
        private Boolean isActive = true;

        @CreationTimestamp
        @Column(name = "created_on", updatable = false)
        private LocalDateTime createdOn;

        @UpdateTimestamp
        @Column(name = "updated_on")
        private LocalDateTime updatedOn;

        @Column(name = "last_update_remarks")
        private String lastUpdateRemarks;

}
