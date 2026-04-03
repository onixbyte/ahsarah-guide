package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;

public record FirearmResponse(
        Long id,
        String name,
        FirearmType type,
        String level,
        String review
) {
    public static FirearmResponse from(Firearm firearm) {
        return new FirearmResponse(
                firearm.getId(),
                firearm.getName(),
                firearm.getType(),
                firearm.getLevel(),
                firearm.getReview()
        );
    }
}

