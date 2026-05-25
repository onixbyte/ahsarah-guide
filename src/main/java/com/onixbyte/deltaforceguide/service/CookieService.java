package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.manager.CookieManager;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    private final CookieManager cookieManager;

    public CookieService(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    public ResponseCookie buildCookie(String cookieName, String value) {
        return buildCookieInternal(cookieName, value, cookieManager.getMaxAge());
    }

    public ResponseCookie buildCookie(String cookieName, String value, Duration validDuration) {
        return buildCookieInternal(cookieName, value, validDuration);
    }

    /**
     * Creates a response cookie builder with specified name, value and valid duration.
     *
     * @param name   name of the cookie
     * @param value  value of the cookie
     * @param maxAge valid duration of the cookie
     * @return cookie builder
     */
    protected ResponseCookie buildCookieInternal(
            String name,
            String value,
            Duration maxAge
    ) {
        return ResponseCookie.from(name, value)
                .secure(cookieManager.getSecure())
                .maxAge(maxAge)
                .httpOnly(cookieManager.getHttpOnly())
                .path(cookieManager.getPath())
                .sameSite(cookieManager.getSameSite().attributeValue())
                .build();
    }
}
