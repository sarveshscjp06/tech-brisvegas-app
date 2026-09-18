package com.brisvegastech.service;

import com.brisvegastech.dto.EnrollRequest;
import com.brisvegastech.entity.PasswordResetToken;
import com.brisvegastech.entity.UserEntity;
import com.brisvegastech.repository.TokenRepository;
import com.brisvegastech.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository; // Your JPA PasswordResetToken repository

    @Autowired
    private JavaMailSender mailSender;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String enrollUser(EnrollRequest request) {
        // 1. Check if the username is already taken
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already enrolled!");
        }

        // 2. Map DTO to Entity and hash the plaintext password
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // Crucial security step
        newUser.setEmail(request.getEmail());
        newUser.setMobile(request.getMobile());
        
        // Assign default 'USER' role if none are provided
        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            newUser.setRoles(Set.of("GUEST_USER"));
        } else {
            newUser.setRoles(request.getRoles());
        }
        
        // Assign default 'ACTIVE' status if none are provided
        if (request.getStatus()!= null || !request.getStatus().isEmpty()) {
            newUser.setStatus(request.getStatus().toString());
        }

        // 3. Save to database
        userRepository.save(newUser);
        return "User enrolled successfully!";
    }

    @Transactional
    public boolean updateUserForEmailVerification(String userName, String email) {
        UserEntity entity = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NullPointerException("data not found"));

        if (!entity.getEmail().equalsIgnoreCase(email)) {
            throw new NullPointerException("email not found");
        }
        entity.setEmailVerified(1);
        UserEntity updated = userRepository.save(entity);
        return true;
    }

    @Transactional
    public boolean updateUserForMobileVerification(String userName, String email, Long mobile) {
        UserEntity entity = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NullPointerException("data not found"));

        if (!entity.getEmail().equalsIgnoreCase(email)) {
            throw new NullPointerException("email not found");
        }
        entity.setMobileVerified(1);
        UserEntity updated = userRepository.save(entity);
        return true;
    }

    // --- FORGOT PASSWORD LOGIC ---

    public void createPasswordResetTokenAndSendEmail(String email) {
        // 1. Look up user by email
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            System.out.println("email not found: Exit silently to prevent user enumeration security holes");
            return; // Exit silently to prevent user enumeration security holes
        }
        UserEntity user = userOptional.get();

        // 2. Generate a secure, unique token
        String token = UUID.randomUUID().toString();
        
        // 3. Save or update the token in the database (expires in 15 minutes)
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        // 4. Send the email
        String resetUrl = "http://140.238.250.40:8888/api/auth/reset-password?token=" + token;
        String emailContent = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetUrl + "\">Reset My Password</a></p>"
                + "<br>"
                + "<p>Note: This link will expire in 15 minutes.</p>";

        sendHtmlEmail(user.getEmail(), "Password Reset Request", emailContent);
    }

    public boolean updatePasswordWithToken(String token, String newPassword) {
        // 1. Validate the token exists
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOptional.get();

        // 2. Check if the token has expired
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(resetToken); // Clean up expired token
            return false;
        }

        // 3. Update the user's password with a hashed version
        UserEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 4. Delete the token so it cannot be reused
        tokenRepository.delete(resetToken);
        return true;
    }

    // --- FORGOT USERNAME LOGIC ---

    public void sendUsernameEmail(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return; // Exit silently
        }
        UserEntity user = userOptional.get();

        String emailContent = "<p>Hello,</p>"
                + "<p>You requested a reminder of your login credentials.</p>"
                + "<p>Your registered username is: <strong>" + user.getUsername() + "</strong></p>";

        sendHtmlEmail(user.getEmail(), "Your Username Reminder", emailContent);
    }

    // --- HELPER METHOD TO SEND EMAIL ---

    private void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom("no-reply@brisvegastech.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // Setting second parameter to true enables HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            // Log the error in production logs
            throw new RuntimeException("Failed to send email", e);
        }
    }
}