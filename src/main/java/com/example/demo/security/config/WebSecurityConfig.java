package com.example.demo.security.config;


import com.example.demo.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final AppUserService appUserService; // inject appUserService
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // inject bCryptPasswordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // disable csrf
                .authorizeRequests() // authorize all requests
                .antMatchers(   "/api/v*/registration/**") // allow registration
                .permitAll() // allow all
                .anyRequest() // any request
                .authenticated().and() // must be authenticated
                .formLogin(); // use form login
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider()); // use daoAuthenticationProvider
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(); // create provider
        provider.setPasswordEncoder(bCryptPasswordEncoder);  // set password encoder
        provider.setUserDetailsService(appUserService); // set user details service
        return provider;
    }
}
