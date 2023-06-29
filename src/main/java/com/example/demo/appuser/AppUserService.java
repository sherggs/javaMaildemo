package com.example.demo.appuser;

import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {


    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
         return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
         // throw exception if user not found
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail()) // check if user exists
                .isPresent();
        if(userExists) {
            throw new IllegalStateException("email already taken"); // throw exception if user already exists
        }

        String encodedPassword =  bCryptPasswordEncoder
                .encode(appUser.getPassword()); // encode password

        appUser.setPassword(encodedPassword); // set password to encoded password

        appUserRepository.save(appUser); // save user to db

        //TODO: Send confirmation token
        String token = UUID.randomUUID().toString(); // generate random token

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(), // token created at current time
                LocalDateTime.now().plusMinutes(15), // token expires in 15 minutes
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken); // save token to db

        // TODO: Send email

        return token;

    }
}
