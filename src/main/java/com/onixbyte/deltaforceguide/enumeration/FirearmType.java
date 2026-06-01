package com.onixbyte.deltaforceguide.enumeration;

/**
 * Enumeration of firearm types in the Delta Force game.
 * Each type is associated with an integer code used for database persistence.
 *
 * @author zihluwang
 */
public enum FirearmType {

    RIFLE(0),
    SUB_MACHINE_GUN(1),
    SHOTGUN(2),
    LIGHT_MACHINE_GUN(3),
    DESIGNATED_MARKSMAN_RIFLE(4),
    SNIPER_RIFLE(5),
    PISTOL(6),
    SPECIAL(7);

    private final int code;

    FirearmType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    /**
     * Resolve a FirearmType from its integer code.
     *
      * @param code the integer code, may be null
     * @return the corresponding FirearmType, or null if the code is null
     * @throws IllegalArgumentException if the code does not match any known type
     */
    public static FirearmType fromCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (FirearmType type : values()) {
            if (type.code == code) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown FirearmType code: " + code);
    }
}
