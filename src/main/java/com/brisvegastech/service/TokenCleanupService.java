package com.brisvegastech.service;

import com.example.demo.repository.TokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {

    private static final Logger log = LoggerFactory.getLogger(TokenCleanupService.class);

    @Autowired
    private TokenRepository tokenRepository;

    /*
    * Run every hour:@Scheduled(cron = "0 0 * * * ?")
    * Run every 30 minutes (Fixed Rate):@Scheduled(fixedRate = 1800000) (Value is in milliseconds) 
     */
    // This cron expression runs every day at midnight (00:00:00)
    // Format: second minute hour day-of-month month day-of-week
    @Scheduled(cron = "0 0 0 * * ?")
    public void purgeExpiredTokens() {
        log.info("Starting background cleanup of expired password reset tokens...");
        
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteAllExpiredSince(now);
        
        log.info("Expired tokens successfully purged.");
    }
}
