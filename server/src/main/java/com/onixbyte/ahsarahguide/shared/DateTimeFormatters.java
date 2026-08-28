package com.onixbyte.ahsarahguide.shared;

import java.time.format.DateTimeFormatter;

/**
 * Utility class providing predefined {@link DateTimeFormatter} instances for common date and
 * time patterns. These formatters can be used to parse and format {@code java.time} objects
 * consistently throughout an application.
 *
 * @author zihluwang
 */
public class DateTimeFormatters {

    /**
     * A {@link DateTimeFormatter} for formatting and parsing full date and time with seconds, using
     * the pattern "yyyy-MM-dd HH:mm:ss".
     * <p>
     * Example: "{@code 2023-10-27 15:30:45}"
     */
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * A {@link DateTimeFormatter} for formatting and parsing dates only, using the
     * pattern "yyyy-MM-dd".
     * <p>
     * Example: "{@code 2023-10-27}"
     */
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * A {@link DateTimeFormatter} for formatting and parsing times only, using the
     * pattern "HH:mm:ss".
     * <p>
     * Example: "{@code 15:30:45}"
     */
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * A {@link DateTimeFormatter} for formatting and parsing year and month only, using the
     * pattern "yyyy-MM".
     * <p>
     * Example: "{@code 2023-10}"
     */
    public static final DateTimeFormatter YEAR_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
}

