package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record SendVerificationCodeRequest(
        @NotBlank(message = "用户名不能为空") String username,
        @NotBlank(message = "电子邮箱地址不能为空") String email
) {
}
