package com.coplaca.apirest.service.impl;

import com.coplaca.apirest.dto.request.LoginRequestDto;
import com.coplaca.apirest.repository.UserRepository;
import com.coplaca.apirest.service.AuthService;

public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean loginClient(LoginRequestDto loginRequest) {
        
        return true;

    }
    
}
