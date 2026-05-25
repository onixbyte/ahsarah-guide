package com.onixbyte.deltaforceguide.domain.entity;

import java.util.ArrayList;
import java.util.List;

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
}
