package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.Accessory;

import java.util.List;

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

