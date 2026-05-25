package com.onixbyte.deltaforceguide.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateTimeUtil {

    public static Instant asInstant(LocalDateTime ldt) {
        return ldt.atZone(ZoneId.systemDefault())
                .toInstant();
    }
}
