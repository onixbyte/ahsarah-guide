package com.onixbyte.deltaforceguide.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO containing daily password data including update information and password list.
 *
 * @author zihluwang
 */
public record DailyPasswordData(
        String updateDate,
        Integer totalCount,
        List<DailyPassword> passwords,
        String source,
        LocalDateTime lastUpdated,
        Long timestamp
) {
}
