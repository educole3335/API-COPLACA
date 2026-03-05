package com.coplaca.apirest.service;

import com.coplaca.apirest.dto.request.LoginRequestDto;

public interface AuthService {

    boolean loginClient(LoginRequestDto loginRequest);
    
}
