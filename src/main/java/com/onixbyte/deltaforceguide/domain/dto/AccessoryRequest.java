package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public record AccessoryRequest(
        @NotBlank(message = "插槽名称不能为空")
        String slotName,
        @NotBlank(message = "配件名称不能为空")
        String accessoryName,
        List<@Valid TuningRequest> tunings
) {
    public List<TuningRequest> tunings() {
        return tunings == null ? new ArrayList<>() : tunings;
    }
}

