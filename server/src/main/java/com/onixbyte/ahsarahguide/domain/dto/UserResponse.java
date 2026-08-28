package com.onixbyte.ahsarahguide.domain.dto;

import com.onixbyte.ahsarahguide.domain.entity.User;

import java.time.LocalDateTime;

/**
 * Response DTO for a user account, including associated credentials.
 *
 * @author zihluwang
 */
public record UserResponse(
        Long id,
        String username,
        String email,
        LocalDateTime expiration
) {
    public static UserResponse from(User user, LocalDateTime expiration) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                expiration
        );
    }
}
