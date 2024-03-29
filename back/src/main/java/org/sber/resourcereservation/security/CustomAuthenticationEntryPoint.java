package org.sber.resourcereservation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sber.resourcereservation.advice.CustomErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Класс представляет собой точку входа аутентификации.
 * Используется для обработки случаев, когда аутентификация не прошла успешно.
 * Не показывает в браузере всплывающего окна с требованием аутентификации.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorResponse.setPath(request.getServletPath());
        String msg = objectMapper.writeValueAsString(errorResponse);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(msg);
    }
}
