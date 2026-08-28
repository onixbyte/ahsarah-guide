package com.onixbyte.ahsarahguide.domain.dto;

public record EmailVerificationCode(
        String username,
        String email,
        String code
) {
}
