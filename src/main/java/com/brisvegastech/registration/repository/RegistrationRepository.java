package com.brisvegastech.registration.repository;

import com.brisvegastech.registration.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {

    Optional<RegistrationEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT r FROM RegistrationEntity r WHERE r.status = :status")
    java.util.List<RegistrationEntity> findAllByStatus(@Param("status") RegistrationEntity.RegistrationStatus status);

}
