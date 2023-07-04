package com.example.demo.registration;

import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.email.EmailSender;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator
                .test(request.getEmail());
        if(!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }

        String token = appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        ); // this is the method we created in AppUserService.java
        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        EmailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(), link));
        return token;
    }
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if(confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if(expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "confirmed";
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family: Arial, Helvetica, sans-serif;\">\n" +
                "    <h1 style=\"text-align: center; color: #5e9ca0;\">Welcome to the Demo App!</h1>\n" +
                "    <p style=\"text-align: center; color: #5e9ca0; font-size: 18px;\">Hi " + name + ",</p>\n" +
                "    <p style=\"text-align: center; color: #5e9ca0; font-size: 18px;\">Thank you for registering with us. Please click on the link below to confirm your email address:</p>\n" +
                "    <p style=\"text-align: center; font-size: 18px;\"><a style=\"color: #5e9ca0;\" href=\"" + link + "\">Confirm my email address</a></p>\n" +
                "    <p style=\"text-align: center; color: #5e9ca0; font-size: 18px;\">We look forward to seeing you soon!</p>\n" +
                "    <p style=\"text-align: center; color: #5e9ca0; font-size: 18px;\">The Demo App Team</p>\n" +
                "</div>";
    }
}
