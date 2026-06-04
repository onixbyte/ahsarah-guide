package com.onixbyte.deltaforceguide.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing an accessory attached to a modification, stored as JSONB.
 *
 * @author zihluwang
 */
public class Accessory {

    private String slotName;

    private String accessoryName;

    private List<Tuning> tunings = new ArrayList<>();

    public Accessory() {
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getAccessoryName() {
        return accessoryName;
    }

    public void setAccessoryName(String accessoryName) {
        this.accessoryName = accessoryName;
    }

    public List<Tuning> getTunings() {
        return tunings;
    }

    public void setTunings(List<Tuning> tunings) {
        this.tunings = tunings;
    }

    public void addTuning(Tuning tuning) {
        this.tunings.add(tuning);
    }

    public void removeTuning(Tuning tuning) {
        this.tunings.remove(tuning);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accessory accessory)) {
            return false;
        }
        return Objects.equals(slotName, accessory.slotName)
                && Objects.equals(accessoryName, accessory.accessoryName)
                && Objects.equals(tunings, accessory.tunings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotName, accessoryName, tunings);
    }
}
