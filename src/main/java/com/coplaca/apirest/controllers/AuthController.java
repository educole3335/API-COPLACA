package com.coplaca.apirest.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.entity.Token;
import com.coplaca.apirest.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping(value = "/loginClient",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> loginClient(@RequestBody LoginRequest loginRequest) {
        
        try {

            Token token = authService.loginClient(loginRequest);

            return new ResponseEntity<>(Map.of("token", token.getToken(), "user", token.getUser()), HttpStatus.OK);

        }catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

}
