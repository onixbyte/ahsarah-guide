package com.onixbyte.deltaforceguide.domain.dto;

/**
 * Standard error response body returned on API failures.
 *
 * @author zihluwang
 */
public record ErrorResponse(
        String message
) {
}

