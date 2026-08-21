package com.onixbyte.deltaforceguide.domain.dto;

public record EmailVerificationCode(
        String username,
        String email,
        String code
) {
}
