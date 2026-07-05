/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.brisvegastech.memosoft;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rudra
 */
@RestController
public class BrisvegastechMemosoft {

    @RequestMapping("/brisvegastech/memosoft")
    public String home() {
        return "<h1>Spring Boot Hello World!</h1><br/> This service is brisvegastech's memosoft Application";
    }

}
