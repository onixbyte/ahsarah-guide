package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "登录名称不能为空") String principle,
        @NotBlank(message = "登录口令不能为空") String credential
) {
}
