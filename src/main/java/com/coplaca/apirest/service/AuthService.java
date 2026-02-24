package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.request.LoginRequestDto;
import com.coplaca.apirest.entities.Client;

public interface AuthService {

    Boolean loginClient(LoginRequestDto loginRequest);
    
}
