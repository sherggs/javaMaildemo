package com.example.demo.registration.token;

import com.example.demo.appuser.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor // Lombok annotation

public class ConfirmationToken {
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )

    private Long id;
    @Column(nullable = false) // this is a constraint
    private String token; // this is the token that we are going to send to the user
    @Column(nullable = false)
    private LocalDateTime createdAt; // this is the time when the token was created
    @Column(nullable = false)
    private LocalDateTime expiresAt; // this is the time when the token will expire
    private LocalDateTime confirmedAt; // this is the time when the token was confirmed

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "app_user_id"
    )
    private AppUser appUser;


    public ConfirmationToken(String token,
                             LocalDateTime createdAt, // this is the time when the token was created
                             LocalDateTime expiresAt, //  this is the time when the token will expire
                             LocalDateTime confirmedAt,
                             AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
        this.appUser = appUser;
    }
}
