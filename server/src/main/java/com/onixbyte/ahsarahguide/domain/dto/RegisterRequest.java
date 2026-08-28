package com.onixbyte.ahsarahguide.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Registration request containing the new user account information.
 *
 * @author zihluwang
 */
@Schema(description = "注册请求")
public record RegisterRequest(
        @NotBlank(message = "用户名不能为空")
        @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
        String username,
        @NotBlank(message = "邮箱不能为空")
        @Email(message = "邮箱格式不正确")
        @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
        String email,
        @Schema(description = "昵称")
        String nickname,
        @Schema(description = "头像链接")
        String avatarUrl,
        @NotBlank(message = "密码不能为空")
        @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
        String password,
        @NotBlank(message = "验证码 ID 不能为空") String verificationCodeId,
        @NotBlank(message = "验证码不能为空") String verificationCode
) {
}
