/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brisvegastech;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author rudra
 */

@RestController
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.brisvegastech.registration"
})
public class BrisvegastechApplication {

    public static void main(String[] args) {
        try {
            UCPDataSource uds = new UCPDataSource();
            uds.testConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SpringApplication.run(BrisvegastechApplication.class, args);
    }

    @RequestMapping
    public void home(HttpServletResponse response) throws IOException {
        // Redirection handled via standard servlet mechanism
        response.sendRedirect("/hello-user.html");
    }

    @GetMapping("/brisvegastech")
    public void redirectToHtml(HttpServletResponse response) throws IOException {
        // Redirection handled via standard servlet mechanism
        response.sendRedirect("/brisvegastech_web.html");
    }
}
