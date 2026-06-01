package com.onixbyte.deltaforceguide.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for date and time operations using system-default time zone.
 *
 * @author zihluwang
 */
public class DateTimeUtil {

    /**
     * Convert a {@link LocalDateTime} to an {@link Instant} using the system-default time zone.
     *
     * @param ldt the local date-time to convert
     * @return the corresponding instant
     */
    public static Instant asInstant(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
