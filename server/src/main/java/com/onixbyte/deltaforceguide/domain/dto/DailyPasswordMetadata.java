package com.onixbyte.deltaforceguide.domain.dto;

/**
 * DTO holding metadata about the daily password source and update tracking.
 *
 * @author zihluwang
 */
public record DailyPasswordMetadata(
        String version,
        String author
) {
}
