package com.coplaca.apirest.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.entity.Token;
import com.coplaca.apirest.entity.User;
import com.coplaca.apirest.repository.TokenRepository;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    TokenRepository tokenRepository;

    public AuthServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Token loginClient(LoginRequest loginRequest) {
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            Token token = new Token();
            token.setUser(user);
            token.setToken(UUID.randomUUID().toString());
            token.setExpirationDate(LocalDateTime.now().plusHours(5));
            token.setCreationDate(LocalDateTime.now());
            token.setActive(true);

            return tokenRepository.save(token);

        } else {
            throw new RuntimeException("Invalid password");
        }


    }
    
}
