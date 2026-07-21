package com.onixbyte.deltaforceguide.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Request record for updating the current user's profile information.
 *
 * @author zihluwang
 */
public record UpdateProfileRequest(
        @Size(min = 1, max = 64, message = "昵称长度必须在1-64个字符之间")
        String nickname,

        @Email(message = "请输入正确的电子邮箱地址")
        @Size(max = 255, message = "电子邮箱长度不能超过255个字符")
        String email
) {}
