package com.coplaca.apirest.service;

public interface TokenService {

    String createToken(String email);

    String validateTokenAndGetEmail(String token);

}
