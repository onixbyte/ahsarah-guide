package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.ErrorResponse;
import com.onixbyte.deltaforceguide.exeption.BizException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException exception) {
        var status = exception.getStatus();
        return ResponseEntity.status(status)
                .body(new ErrorResponse(exception.getMessage()));
    }
}

