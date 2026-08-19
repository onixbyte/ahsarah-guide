package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.*;
import com.onixbyte.deltaforceguide.security.annotation.RequiresAuth;
import com.onixbyte.deltaforceguide.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user authentication endpoints (login, logout).
 *
 * @author zihluwang
 */
@Tag(name = "用户鉴权", description = "处理用户登录与退出功能")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(description = "用户登录")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Validated @RequestBody LoginRequest request) {
        var user = authService.login(request);
        return authService.getUserResponseResponseEntity(user);
    }

    @RequiresAuth
    @Operation(description = "退出登录")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        var expiredCookie = authService.logout();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expiredCookie.toString())
                .build();
    }

    @Operation(description = "发送邮件验证码")
    @PostMapping("/verification-code")
    public SendVerificationCodeResponse sendVerificationCode(@Validated @RequestBody SendVerificationCodeRequest request) {
        var verificationCodeId = authService.sendVerificationCode(request);
        return new SendVerificationCodeResponse(verificationCodeId);
    }

    @Operation(description = "用户注册")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Validated @RequestBody RegisterRequest request) {
        var user = authService.register(request);
        return authService.getUserResponseResponseEntity(user);
    }
}
