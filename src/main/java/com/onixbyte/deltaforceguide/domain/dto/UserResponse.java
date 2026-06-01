package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.User;

/**
 * Response DTO for a user account, including associated credentials.
 *
 * @author zihluwang
 */
public record UserResponse(
        Long id,
        String username,
        String email
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
