package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TuningRequest(
        @NotBlank(message = "调校项名称不能为空")
        String tuningName,
        @NotNull(message = "调校值不能为空")
        Double tuningValue
) {
}

