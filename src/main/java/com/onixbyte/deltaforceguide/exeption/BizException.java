package com.onixbyte.deltaforceguide.exeption;

import org.springframework.http.HttpStatus;

public class BizException extends RuntimeException {

    /**
     * The HTTP status code associated with this business exception.
     * <p>
     * This status code indicates the appropriate HTTP response status that should be returned to
     * clients when this exception occurs. It enables consistent error handling across
     * REST API endpoints.
     */
    private final HttpStatus status;

    public BizException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public BizException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Returns the HTTP status code associated with this business exception.
     *
     * @return the HTTP status code that should be used in the error response
     */
    public HttpStatus getStatus() {
        return status;
    }
}

