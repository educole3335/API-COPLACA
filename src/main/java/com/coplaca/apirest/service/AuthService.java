package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.LoginRequest;
import com.coplaca.apirest.entity.Token;

public interface AuthService {

    Token loginClient(LoginRequest loginRequest);
    
}
