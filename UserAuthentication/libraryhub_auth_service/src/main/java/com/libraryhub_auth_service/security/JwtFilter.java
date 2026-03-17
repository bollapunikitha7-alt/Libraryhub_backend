package com.libraryhub_auth_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication
        .UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority
        .SimpleGrantedAuthority;
import org.springframework.security.core.context
        .SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Step 1 — Get Authorization header
        String authHeader =
            request.getHeader("Authorization");

        // Step 2 — If no token, skip this filter
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3 — Extract token from header
        String token = authHeader.substring(7);

        // Step 4 — If token invalid, skip this filter
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5 — Get email and role from token
        String email = jwtUtil.extractEmail(token);
        String role  = jwtUtil.extractRole(token);

        // Step 6 — Set authentication in Spring Security
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                email,
                null,
                List.of(new SimpleGrantedAuthority(
                    "ROLE_" + role))
            );

        SecurityContextHolder
            .getContext()
            .setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}