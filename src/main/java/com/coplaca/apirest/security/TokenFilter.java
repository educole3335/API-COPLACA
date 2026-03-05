package com.coplaca.apirest.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.coplaca.apirest.entity.Token;
import com.coplaca.apirest.repository.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired 
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String tokenValue = authHeader.substring(7);

            Token token = tokenRepository.findById(tokenValue).orElseThrow(() -> new RuntimeException("Token no encontrado"));


            if (token.getExpirationDate().isAfter(LocalDateTime.now())) {
                    request.setAttribute("authenticatedUser", token.getUser());
                    
                    filterChain.doFilter(request, response);
                    return;
            }
            
        }

        if (request.getRequestURI().startsWith("/api/private")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Acceso denegado: Token invalido o ausente");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}