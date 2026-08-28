package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.domain.entity.Firearm;
import com.onixbyte.deltaforceguide.enumeration.FirearmType;

/**
 * Response DTO for a firearm record, including associated modifications.
 *
 * @author zihluwang
 */
public record FirearmResponse(
        Long id,
        String name,
        FirearmType type,
        String level,
        String calibre,
        Integer fireRate,
        Integer armourDamage,
        Integer bodyDamage,
        String review
) {
    public static FirearmResponse from(Firearm firearm) {
        return new FirearmResponse(
                firearm.getId(),
                firearm.getName(),
                firearm.getType(),
                firearm.getLevel(),
                firearm.getCalibre(),
                firearm.getFireRate(),
                firearm.getArmourDamage(),
                firearm.getBodyDamage(),
                firearm.getReview()
        );
    }
}

