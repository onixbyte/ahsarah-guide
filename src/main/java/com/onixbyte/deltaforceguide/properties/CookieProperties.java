package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.web.server.Cookie;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.cookie")
public record CookieProperties(
        @DefaultValue("true") Boolean httpOnly,
        @DefaultValue("true") Boolean secure,
        @DefaultValue("/") String path,
        @DefaultValue("PT2H") Duration maxAge,
        @DefaultValue("LAX") Cookie.SameSite sameSite
) {
}
