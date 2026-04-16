package com.onixbyte.deltaforceguide.domain.dto;

import com.onixbyte.deltaforceguide.enumeration.FirearmType;

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
