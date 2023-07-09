package com.example.demo.registration;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String firstName; // this is the same as the one in AppUser.java
    private final String lastName; // this is the same as the one in AppUser.java
    private final String email;
    private final String password;

}
