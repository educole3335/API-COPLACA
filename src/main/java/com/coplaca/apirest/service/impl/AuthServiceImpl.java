package com.coplaca.apirest.service.impl;

import com.coplaca.apirest.dto.request.LoginRequestDto;
import com.coplaca.apirest.repositories.ClientRepository;
import com.coplaca.apirest.service.AuthService;

public class AuthServiceImpl implements AuthService {

    ClientRepository clientRepository;

    public AuthServiceImpl(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @Override
    public Boolean loginClient(LoginRequestDto loginRequest) {
        
        return null;

    }
    
}
