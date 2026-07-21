package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.BuildSummaryResponse;
import com.onixbyte.deltaforceguide.domain.dto.ChangePasswordRequest;
import com.onixbyte.deltaforceguide.domain.dto.PageResponse;
import com.onixbyte.deltaforceguide.domain.dto.UpdateProfileRequest;
import com.onixbyte.deltaforceguide.domain.dto.UserProfileResponse;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.security.annotation.RequiresAuth;
import com.onixbyte.deltaforceguide.security.resolver.CurrentUser;
import com.onixbyte.deltaforceguide.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the current user's profile management endpoints.
 *
 * @author zihluwang
 */
@Tag(name = "用户个人中心", description = "当前用户的资料管理与密码修改")
@RestController
@RequestMapping("/user")
@Validated
@RequiresAuth
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(description = "获取当前登录用户的个人资料")
    @GetMapping("/me")
    public UserProfileResponse profile(@CurrentUser User user) {
        return userService.getProfile(user);
    }

    @Operation(description = "修改当前用户的个人资料（昵称与邮箱）")
    @PutMapping("/me")
    public UserProfileResponse updateProfile(
            @CurrentUser User user,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateProfile(user, request);
    }

    @Operation(description = "修改当前用户的登录密码")
    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @CurrentUser User user,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(user, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "分页查询当前用户的改枪码列表")
    @GetMapping("/me/builds")
    public PageResponse<BuildSummaryResponse> builds(
            @CurrentUser User user,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size
    ) {
        return userService.getBuilds(user, PageRequest.of(page, size));
    }
}
