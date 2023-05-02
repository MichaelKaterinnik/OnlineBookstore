package com.onlinebookstore.security;

import com.onlinebookstore.domain.UserEntity;
import com.onlinebookstore.models.AuthentificationRequest;
import com.onlinebookstore.models.AuthentificationResponse;
import com.onlinebookstore.models.RegisterRequest;
import com.onlinebookstore.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {
    @Autowired
    private UsersService usersService;
    @Autowired
    private AuthenticationManager authentificationManager;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenService jwtTokenService;


    public AuthentificationResponse register(RegisterRequest registerRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(registerRequest.getEmail());
        userEntity.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userEntity.setRole(UserEntity.Role.USER);

        CustomUserDetails customUserDetails = new CustomUserDetails(usersService.save(userEntity));

        return new AuthentificationResponse(jwtTokenService.generateToken(customUserDetails));
    }

    public AuthentificationResponse login(AuthentificationRequest registerRequest) {
        Authentication authentication = authentificationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registerRequest.getEmail(), registerRequest.getPassword())
        );

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        UserEntity user = usersService.findByEmail(registerRequest.getEmail());

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        return new AuthentificationResponse(jwtTokenService.generateToken(customUserDetails));
    }
}
