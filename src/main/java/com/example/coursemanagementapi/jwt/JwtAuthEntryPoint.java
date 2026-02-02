package com.example.coursemanagementapi.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        log.error("Authentication failed: {}", authException.getMessage());
        log.error(">>> JwtAuthEntryPoint triggered: {}", authException.getClass().getSimpleName());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        int status;
        String type;
        String details;

        if (authException instanceof UsernameNotFoundException e) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            type = "UserNotFound";
            details = e.getMessage();
        } else if (authException instanceof BadCredentialsException e) {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            type = "InvalidCredentials";
            details = e.getMessage();
        } else if (authException instanceof InsufficientAuthenticationException e) {
            status = HttpServletResponse.SC_BAD_REQUEST;
            type = "BadRequest";
            details = "Something Went Wrong!";
        } else if (authException instanceof DisabledException e) {
            status = HttpServletResponse.SC_FORBIDDEN;
            type = "AccountDisabled";
            details = e.getMessage();
        } else if (authException instanceof LockedException e) {
            status = HttpServletResponse.SC_FORBIDDEN;
            type = "AccountLocked";
            details = e.getMessage();
        } else if (authException instanceof AccountExpiredException e) {
            status = HttpServletResponse.SC_FORBIDDEN;
            type = "AccountExpired";
            details = e.getMessage();
        } else if (authException instanceof AuthenticationServiceException e) {
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            type = "AuthServiceError";
            details = e.getMessage();
        } else {
            status = HttpServletResponse.SC_UNAUTHORIZED;
            type = "AuthenticationFailed";
            details = authException.getMessage();
        }

        DateTimeFormatter readableFormatter = DateTimeFormatter
                .ofPattern("EEEE, dd MMMM yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        Instant now = Instant.now();

        String path = request.getRequestURI();
        if (request.getQueryString() != null) {
            path += "?" + request.getQueryString();
        }

        // Convert type to SCREAMING_SNAKE_CASE
        String typeSnakeCase = type.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();

        Map<String, Object> error = new LinkedHashMap<>();
        error.put("code", status);
        error.put("type", typeSnakeCase);
        error.put("errorMessage", details != null ? details : "Authentication failed.");
        error.put("path", path);
        error.put("timestamp", now.toString());

        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("errorDate", readableFormatter.format(now));
        responseBody.put("success", false);
        responseBody.put("errors", error);

        response.setStatus(status);
        new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValue(response.getOutputStream(), responseBody);
        response.flushBuffer();
    }

}
