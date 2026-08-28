package com.onixbyte.ahsarahguide.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request record for managing a user's role assignment.
 *
 * @author zihluwang
 */
public record ManageRoleRequest(
        @NotBlank(message = "角色不能为空")
        String role
) {}
