package com.onixbyte.deltaforceguide.domain.dto;

import java.util.List;

/**
 * Response record summarising a user's custom modification build.
 * This is a skeleton record whose {@code from()} factory will be fully implemented
 * when the CustomModification entity is introduced in a future issue.
 *
 * @author zihluwang
 */
public record BuildSummaryResponse(
        Long id,
        String name,
        String code,
        String firearmName,
        List<String> tags
) {
    // from() factory to be implemented when CustomModification entity lands
}
