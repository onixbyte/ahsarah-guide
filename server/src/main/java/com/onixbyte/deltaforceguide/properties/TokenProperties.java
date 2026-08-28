package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

/**
 * Configuration properties for JWT token generation and validation, prefixed with "app.jwt".
 *
 * @param issuer    the JWT issuer claim
 * @param secret    the signing secret for JWT tokens
 * @param validTime the duration for which a token remains valid
 * @author zihluwang
 */
@ConfigurationProperties(prefix = "app.jwt")
public record TokenProperties(
        String issuer,
        String secret,
        @DefaultValue("PT2H") Duration validTime,
        @DefaultValue("PT5M") Duration renewThreshold
) {
}
