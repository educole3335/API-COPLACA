package com.coplaca.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coplaca.apirest.entity.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
    
}
