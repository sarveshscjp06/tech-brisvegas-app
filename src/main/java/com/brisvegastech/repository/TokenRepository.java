package com.brisvegastech.repository;

import com.brisvegastech.entity.PasswordResetToken;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    // @Modifying and @Transactional are required for delete or update queries
    //file name (PasswordResetToken) is not table name
    @Modifying
    @Transactional
    @Query("DELETE FROM password_reset_tokens t WHERE t.expiryDate <= :now")
    void deleteAllExpiredSince(@Param("now") LocalDateTime now);
}