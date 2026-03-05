package com.coplaca.apirest.service;

public interface JwtService {

    String createToken(String email);

    String validateTokenAndGetEmail(String token);

}
