package com.brisvegastech.registration;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationApplication {

    @RequestMapping("/brisvegastech/registration")
    public String home() {
        return "<h1>Spring Boot Hello World!</h1><br/> This service is brisvegastech's User Registration Application";
    }
}
