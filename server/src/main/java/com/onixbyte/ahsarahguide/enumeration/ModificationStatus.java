package com.onixbyte.ahsarahguide.enumeration;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Enumeration of modification statuses.
 * <p>
 * Each status is associated with an integer code used for database persistence.
 *
 * @author zihluwang
 */
public enum ModificationStatus {

    DRAFT(0),
    PENDING_APPROVAL(1),
    PUBLISHED(2);

    private final int code;

    ModificationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static final Map<Integer, ModificationStatus> CODE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(ModificationStatus::getCode, Function.identity()));

    /**
     * Resolve a ModificationStatus from its integer code.
     *
     * @param code the integer code, may be null
     * @return the corresponding ModificationStatus, or null if the code is null
     * @throws IllegalArgumentException if the code does not match any known status
     */
    public static ModificationStatus fromCode(Integer code) {
        return CODE_MAP.get(code);
    }
}
