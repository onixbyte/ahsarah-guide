package com.onixbyte.deltaforceguide.domain.dto;

public record DailyPasswordResponse(
        String status,
        String message,
        DailyPasswordData data,
        DailyPasswordMetadata metadata
) {
}
