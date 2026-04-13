package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.LoginRequest;
import com.onixbyte.deltaforceguide.domain.dto.UserResponse;
import com.onixbyte.deltaforceguide.client.TokenClient;
import com.onixbyte.deltaforceguide.service.AuthService;
import com.onixbyte.deltaforceguide.service.CookieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TokenClient tokenClient;
    private final CookieService cookieService;

    public AuthController(AuthService authService, TokenClient tokenClient, CookieService cookieService) {
        this.authService = authService;
        this.tokenClient = tokenClient;
        this.cookieService = cookieService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        var user = authService.login(request);
        var accessToken = tokenClient.generateToken(user);
        var accessTokenCookie = cookieService.buildCookie("AccessToken", accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(UserResponse.from(user));
    }
}
