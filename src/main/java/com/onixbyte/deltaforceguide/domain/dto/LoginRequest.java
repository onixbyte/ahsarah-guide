package com.onixbyte.deltaforceguide.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "登录请求")
public record LoginRequest(
        @NotBlank(message = "登录名称不能为空")
        @Schema(description = "用户名或电子邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
        String principle,
        @NotBlank(message = "登录口令不能为空")
        @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
        String credential
) {
}
