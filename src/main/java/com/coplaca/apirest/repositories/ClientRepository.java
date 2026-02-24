package com.coplaca.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coplaca.apirest.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    
    
}