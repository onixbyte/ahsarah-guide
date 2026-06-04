package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.properties.CookieProperties;
import org.springframework.boot.web.server.Cookie;
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
}
