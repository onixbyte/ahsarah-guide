package com.onixbyte.ahsarahguide.domain.dto;

/**
 * Standard error response body returned on API failures.
 *
 * @author zihluwang
 */
public record ErrorResponse(
        String message
) {
}

