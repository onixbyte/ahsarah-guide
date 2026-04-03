package com.onixbyte.deltaforceguide.enumeration;

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
