package com.onixbyte.ahsarahguide.controller;

import com.onixbyte.ahsarahguide.domain.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global exception handler that translates exceptions into standard error responses.
 *
 * @author zihluwang
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException exception) {
        var status = exception.getStatusCode();
        return ResponseEntity.status(status)
                .body(new ErrorResponse(exception.getReason()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("请先登录"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var httpStatus = authentication == null || !authentication.isAuthenticated()
                ? HttpStatus.UNAUTHORIZED
                : HttpStatus.FORBIDDEN;
        var message = httpStatus == HttpStatus.UNAUTHORIZED ? "请先登录" : "权限不足";
        return ResponseEntity.status(httpStatus)
                .body(new ErrorResponse(message));
    }
}

