package com.onixbyte.ahsarahguide.domain.dto;

/**
 * Response DTO wrapping daily password data with metadata.
 *
 * @author zihluwang
 */
public record DailyPasswordResponse(
        String status,
        String message,
        DailyPasswordData data,
        DailyPasswordMetadata metadata
) {
}
