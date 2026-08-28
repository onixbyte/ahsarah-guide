package com.onixbyte.ahsarahguide.domain.dto;

import com.onixbyte.ahsarahguide.domain.entity.Tuning;

/**
 * Response DTO for a tuning adjustment on an accessory.
 *
 * @author zihluwang
 */
public record TuningResponse(
        String tuningName,
        Double tuningValue
) {
    public static TuningResponse from(Tuning tuning) {
        return new TuningResponse(
                tuning.getTuningName(),
                tuning.getTuningValue()
        );
    }
}

