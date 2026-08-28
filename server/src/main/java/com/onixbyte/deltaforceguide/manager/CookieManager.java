package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.properties.CookieProperties;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Manager providing cookie construction operations with configurable properties.
 *
 * @author zihluwang
 */
@Component
public class CookieManager {

    private final CookieProperties cookieProperties;

    public CookieManager(CookieProperties cookieProperties) {
        this.cookieProperties = cookieProperties;
    }

    public Boolean getHttpOnly() {
        return cookieProperties.httpOnly();
    }

    public Boolean getSecure() {
        return cookieProperties.secure();
    }

    public Cookie.SameSite getSameSite() {
        return cookieProperties.sameSite();
    }

    public String getPath() {
        return cookieProperties.path();
    }

    public Duration getMaxAge() {
        return cookieProperties.maxAge();
    }

    /**
     * Builds a response cookie with the default max age from configuration.
     *
     * @param cookieName the cookie name
     * @param value      the cookie value
     * @return a configured ResponseCookie
     */
    public ResponseCookie buildCookie(String cookieName, String value) {
        return buildCookieInternal(cookieName, value, getMaxAge());
    }

    /**
     * Builds a response cookie with a custom valid duration.
     *
     * @param cookieName    the cookie name
     * @param value         the cookie value
     * @param validDuration the cookie's max age
     * @return a configured ResponseCookie
     */
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
                .secure(getSecure())
                .maxAge(maxAge)
                .httpOnly(getHttpOnly())
                .path(getPath())
                .sameSite(getSameSite().attributeValue())
                .build();
    }
}
