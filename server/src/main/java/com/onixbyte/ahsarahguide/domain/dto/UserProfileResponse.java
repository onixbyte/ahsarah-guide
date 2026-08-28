package com.onixbyte.ahsarahguide.domain.dto;

import com.onixbyte.ahsarahguide.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response record for the current user's full profile information.
 *
 * @author zihluwang
 */
public record UserProfileResponse(
        Long id,
        String username,
        String nickname,
        String email,
        String avatarUrl,
        Boolean enabled,
        LocalDateTime createdAt,
        List<String> roles
) {

    /**
     * Creates a {@code UserProfileResponse} from a {@link User} entity and its assigned roles.
     *
     * @param user  the user entity
     * @param roles the list of role names assigned to the user
     * @return the constructed response
     */
    public static UserProfileResponse from(User user, List<String> roles) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname() == null || user.getNickname().isBlank()
                        ? user.getUsername() : user.getNickname(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getEnabled(),
                user.getCreatedAt(),
                roles
        );
    }
}
