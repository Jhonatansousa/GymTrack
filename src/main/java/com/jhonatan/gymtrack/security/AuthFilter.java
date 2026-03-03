package com.jhonatan.gymtrack.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;

    public AuthFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

// Usa a instância recebida no construtor
        Authentication auth = tokenUtil.decodeToken(request);

        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);

    }
}
