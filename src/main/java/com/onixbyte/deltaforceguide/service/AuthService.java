package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.client.TokenClient;
import com.onixbyte.deltaforceguide.domain.dto.LoginRequest;
import com.onixbyte.deltaforceguide.domain.dto.UserResponse;
import com.onixbyte.deltaforceguide.domain.entity.User;
import com.onixbyte.deltaforceguide.exeption.InternalServerErrorException;
import com.onixbyte.deltaforceguide.manager.CookieManager;
import com.onixbyte.deltaforceguide.security.authentication.UsernamePasswordAuthentication;
import com.onixbyte.deltaforceguide.shared.CookieName;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service handling user authentication, login, and session management.
 *
 * @author zihluwang
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final TokenClient tokenClient;
    private final CookieManager cookieManager;

    public AuthService(
            AuthenticationManager authenticationManager,
            TokenClient tokenClient,
            CookieManager cookieManager
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenClient = tokenClient;
        this.cookieManager = cookieManager;
    }

    /**
     * Authenticates a user with the given login credentials.
     * <p>
     * Delegates authentication to Spring Security's {@link AuthenticationManager} and verifies
     * that the result is of the expected {@link UsernamePasswordAuthentication} type.
     *
     * @param request the login credentials containing principle and password
     * @return the authenticated {@link User}
     */
    public User login(LoginRequest request) {
        var _authentication = authenticationManager.authenticate(UsernamePasswordAuthentication
                .unauthenticated(request.principle(), request.credential()));
        if (!(_authentication instanceof UsernamePasswordAuthentication authentication)) {
            log.error(
                    "Type mismatched, required type is UsernamePasswordAuthentication but got {}.",
                    _authentication.getClass()
            );
            throw new InternalServerErrorException("登录服务异常，请稍后再试。");
        }

        return authentication.getDetails();
    }

    @NonNull
    public ResponseEntity<UserResponse> getUserResponseResponseEntity(User user) {
        var currentTime = LocalDateTime.now();
        var accessToken = tokenClient.generateToken(user);
        var accessTokenCookie = cookieManager.buildCookie(CookieName.ACCESS_TOKEN, accessToken);
        var cookieMaxAge = accessTokenCookie.getMaxAge();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .body(UserResponse.from(user, currentTime.plus(cookieMaxAge)));
    }

    public HttpCookie logout() {
        return cookieManager.buildCookie(CookieName.ACCESS_TOKEN, "", Duration.ZERO);
    }
}
