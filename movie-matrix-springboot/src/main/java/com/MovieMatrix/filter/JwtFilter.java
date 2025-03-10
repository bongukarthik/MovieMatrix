package com.movieMatrix.filter;

import com.movieMatrix.exceptions.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.stereotype.Component;

import com.movieMatrix.utils.JwtUtil;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = extractJwtToken(request);

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                processToken(token, request);
            }
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            handleAuthenticationException(response, e);
        } catch (Exception e) {
//            log.error("Cannot set user authentication: {}", e.getMessage());
            handleAuthenticationException(response,
                    new JwtAuthenticationException("Invalid token"));
        }
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void processToken(String token, HttpServletRequest request) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractUserEmail(token);
//            log.debug("Processing token for user: {}", email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (userDetails != null && userDetails.isEnabled()) {
                authenticateUser(userDetails, request);
            }
        }
    }

    private void authenticateUser(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
//        log.debug("User authenticated successfully: {}", userDetails.getUsername());
    }

    private void handleAuthenticationException(
            HttpServletResponse response,
            JwtAuthenticationException e) throws IOException {
//        log.error("Authentication error: {}", e.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Unauthorized");
        error.put("message", e.getMessage());
        objectMapper.writeValue(response.getOutputStream(), error);
    }
}
