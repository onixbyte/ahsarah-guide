package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for a tuning adjustment on an accessory.
 *
 * @author zihluwang
 */
public record TuningRequest(
        @NotBlank(message = "调校项名称不能为空")
        String tuningName,
        @NotNull(message = "调校值不能为空")
        Double tuningValue
) {
}

