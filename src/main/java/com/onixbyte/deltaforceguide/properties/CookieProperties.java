package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.web.server.Cookie;

import java.time.Duration;

/**
 * Configuration properties for HTTP cookies used in authentication, prefixed with "app.cookie".
 *
 * @param httpOnly  whether the cookie is httpOnly
 * @param secure    whether the cookie is secure
 * @param path      the cookie path
 * @param maxAge    the maximum age of the cookie
 * @param sameSite  the SameSite policy for the cookie
 * @author zihluwang
 */
@ConfigurationProperties(prefix = "app.cookie")
public record CookieProperties(
        @DefaultValue("true") Boolean httpOnly,
        @DefaultValue("true") Boolean secure,
        @DefaultValue("/") String path,
        @DefaultValue("PT2H") Duration maxAge,
        @DefaultValue("LAX") Cookie.SameSite sameSite
) {
}
