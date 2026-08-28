package com.onixbyte.deltaforceguide.domain.dto;

/**
 * DTO representing a single daily-generated password for a map.
 *
 * @author zihluwang
 */
public record DailyPassword(
        String mapName,
        String password
) {
}
