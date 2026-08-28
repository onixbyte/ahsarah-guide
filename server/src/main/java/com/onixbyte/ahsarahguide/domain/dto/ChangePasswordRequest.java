package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request record for changing the current user's password.
 *
 * @author zihluwang
 */
public record ChangePasswordRequest(
        @NotBlank(message = "旧密码不能为空")
        String oldPassword,

        @NotBlank(message = "新密码不能为空")
        @Size(min = 8, max = 128, message = "密码长度必须在8-128个字符之间")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "密码必须包含至少一个大写字母、一个小写字母和一个数字"
        )
        String newPassword
) {}
