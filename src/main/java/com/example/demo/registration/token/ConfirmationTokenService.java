package com.example.demo.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository; // this is an interface

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);  // this is a method in the interface


    }

    public void deleteConfirmationToken(Long id) {
        confirmationTokenRepository.deleteById(id); // this is a method in the interface
    }
}
