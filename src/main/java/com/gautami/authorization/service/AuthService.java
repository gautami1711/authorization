package com.gautami.authorization.service;

import com.gautami.authorization.dto.JwtRequest;
import com.gautami.authorization.dto.JwtResponse;
import com.gautami.authorization.exception.InvalidRequest;
import com.gautami.authorization.jwt.JwtAuthenticationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    AuthenticationManager manager;

    JwtAuthenticationHelper jwtHelper;


    UserDetailsService userDetailsService;

    @Autowired
    public AuthService(AuthenticationManager manager,JwtAuthenticationHelper jwtHelper, UserDetailsService userDetailsService){
        this.manager=manager;
        this.jwtHelper=jwtHelper;
        this.userDetailsService=userDetailsService;
    }

    public JwtResponse login(JwtRequest jwtRequest) {
        log.info("starting the user login process to generate token");

        //authenticate with Authentication manager
        this.doAuthenticate(jwtRequest.getUsername(),jwtRequest.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtHelper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder().jwtToken(token).build();
        log.info("Jwt token created!!!!");
        return response;
    }

    private void doAuthenticate(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authenticationToken);
            log.info("User authenticated for token generation");

        }catch (BadCredentialsException e) {
            throw new InvalidRequest("Invalid Username or Password");
        }
    }

}
