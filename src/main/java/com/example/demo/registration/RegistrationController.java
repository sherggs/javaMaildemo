package com.example.demo.registration;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/registration") // http://localhost:8080/api/v1/registration
public class RegistrationController {

    private final RegistrationService registrationService;

    public String register
            (@RequestBody RegistrationRequest request) {
        return registrationService.register(request); // this is the method we created in RegistrationService.java
    }
}
