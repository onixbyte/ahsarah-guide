package com.onixbyte.deltaforceguide.domain.entity;

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
}
