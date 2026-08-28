package com.onixbyte.ahsarahguide.controller;

import com.onixbyte.ahsarahguide.domain.dto.ManageRoleRequest;
import com.onixbyte.ahsarahguide.security.annotation.RequiresAuth;
import com.onixbyte.ahsarahguide.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for administrative user and role management.
 *
 * @author zihluwang
 */
@Tag(name = "管理员", description = "管理员用户与角色管理")
@RestController
@RequestMapping("/admin")
@Validated
@RequiresAuth
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "为用户分配角色（仅超级管理员可操作）")
    @PreAuthorize("hasRole('SUPER_USER')")
    @PutMapping("/users/{id}/roles")
    public ResponseEntity<Void> assignRole(
            @PathVariable @Positive(message = "用户ID必须为正数") Long id,
            @Valid @RequestBody ManageRoleRequest request
    ) {
        userService.assignRole(id, request.role());
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "移除用户角色（仅超级管理员可操作）")
    @PreAuthorize("hasRole('SUPER_USER')")
    @DeleteMapping("/users/{id}/roles/{role}")
    public ResponseEntity<Void> removeRole(
            @PathVariable @Positive(message = "用户ID必须为正数") Long id,
            @PathVariable String role
    ) {
        userService.removeRole(id, role);
        return ResponseEntity.noContent().build();
    }
}
