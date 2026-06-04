package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.enumeration.FirearmType;

/**
 * Request DTO for creating or updating a firearm.
 *
 * @author zihluwang
 */
public record FirearmRequest(
        String name,
        FirearmType type,
        String level,
        String calibre,
        Integer fireRate,
        Integer armourDamage,
        Integer bodyDamage,
        String review
) {
}
