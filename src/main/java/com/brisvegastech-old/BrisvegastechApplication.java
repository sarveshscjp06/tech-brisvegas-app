/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brisvegastech;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rudra
 */
@RestController
public class BrisvegastechApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    @GetMapping("/brisvegastech")
    public void redirectToHtml(HttpServletResponse response) throws IOException {
        // Redirection handled via standard servlet mechanism
        response.sendRedirect("/hello-user.html");
    }
}
