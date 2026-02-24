package com.coplaca.apirest.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coplaca.apirest.dto.request.LoginRequestDto;

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
    public ResponseEntity<?> loginClient(@RequestBody LoginRequestDto loginRequest) {
        
        if(authService.loginClient(loginRequest)) {

        }

        return null;
    }

}
