package com.onixbyte.deltaforceguide.domain.entity;

import java.util.Objects;

public class Tuning {

    private String tuningName;
    private Double tuningValue;

    public Tuning() {
    }

    public String getTuningName() {
        return tuningName;
    }

    public void setTuningName(String tuningName) {
        this.tuningName = tuningName;
    }

    public Double getTuningValue() {
        return tuningValue;
    }

    public void setTuningValue(Double tuningValue) {
        this.tuningValue = tuningValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuning tuning)) {
            return false;
        }
        return Objects.equals(tuningName, tuning.tuningName)
                && Objects.equals(tuningValue, tuning.tuningValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tuningName, tuningValue);
    }
}
