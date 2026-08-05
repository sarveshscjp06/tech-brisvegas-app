package com.brisvegastech.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    // Constructor injection
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a plain text email.
     */
    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    /**
     * Sends an HTML email containing a file attachment.
     */
    public void sendHtmlEmailWithAttachment(String to, String subject, String htmlContent, String filePath) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        
        // Pass 'true' to indicate a multipart message (required for attachments/HTML)
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        // Pass 'true' to specify that the body content is HTML
        helper.setText(htmlContent, true);

        // Add the attachment
        FileSystemResource fileResource = new FileSystemResource(new File(filePath));
        if (fileResource.exists()) {
            helper.addAttachment(fileResource.getFilename(), fileResource);
        } else {
            throw new IllegalArgumentException("Attachment file not found at: " + filePath);
        }

        mailSender.send(message);
    }
}
