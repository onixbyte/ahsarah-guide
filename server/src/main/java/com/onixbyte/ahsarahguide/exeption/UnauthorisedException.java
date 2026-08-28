package com.onixbyte.ahsarahguide.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorisedException extends ResponseStatusException {

    public UnauthorisedException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }
}
