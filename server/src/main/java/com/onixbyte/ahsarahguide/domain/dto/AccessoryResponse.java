package com.onixbyte.ahsarahguide.domain.dto;

import com.onixbyte.ahsarahguide.domain.entity.Accessory;

import java.util.List;

/**
 * Response DTO for an accessory attached to a modification.
 *
 * @author zihluwang
 */
public record AccessoryResponse(
        String slotName,
        String accessoryName,
        List<TuningResponse> tunings
) {
    public static AccessoryResponse from(Accessory accessory) {
        return new AccessoryResponse(
                accessory.getSlotName(),
                accessory.getAccessoryName(),
                accessory.getTunings() == null
                        ? List.of()
                        : accessory.getTunings().stream().map(TuningResponse::from).toList()
        );
    }
}

