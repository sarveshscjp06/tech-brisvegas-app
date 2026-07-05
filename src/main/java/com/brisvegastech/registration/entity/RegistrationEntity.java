package com.brisvegastech.registration.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA entity representing a registration record.
 * Mapped to Oracle sequence-backed primary key generation, which is the
 * standard approach for Oracle (no native IDENTITY support pre-12c,
 * and sequences remain the idiomatic choice on Oracle even in 12c+).
 */
@Entity
@Table(name = "REGISTRATIONS", uniqueConstraints = {
        @UniqueConstraint(name = "UK_REGISTRATION_EMAIL", columnNames = "EMAIL")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "registration_seq_gen")
    @SequenceGenerator(
            name = "registration_seq_gen",
            sequenceName = "REGISTRATION_SEQ",
            allocationSize = 1
    )
    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false, length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 100)
    private String lastName;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "PHONE_NUMBER", length = 20)
    private String phoneNumber;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    @Builder.Default
    private RegistrationStatus status = RegistrationStatus.ACTIVE;

    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum RegistrationStatus {
        ACTIVE, INACTIVE, PENDING_VERIFICATION, SUSPENDED
    }
}
