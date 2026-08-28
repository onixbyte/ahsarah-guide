package com.onixbyte.ahsarahguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpMethod;

import java.time.Duration;

/**
 * Configuration properties for CORS settings, prefixed with "app.cors".
 *
 * @param allowedHeaders      headers allowed in CORS requests
 * @param allowedMethods      HTTP methods allowed in CORS requests
 * @param allowedOrigins      origins permitted to make cross-origin requests
 * @param allowCredentials    whether credentials are allowed in CORS requests
 * @param allowPrivateNetwork whether private network access is permitted
 * @param maxAge              how long the CORS preflight response may be cached
 * @param exposedHeaders      headers exposed to the client in CORS responses
 * @author zihluwang
 */
@ConfigurationProperties(prefix = "app.cors")
public record CorsProperties(
        @DefaultValue({"Content-Type", "Authorization"})
        String[] allowedHeaders,
        @DefaultValue({"GET", "POST", "PUT", "PATCH", "DELETE"})
        HttpMethod[] allowedMethods,
        String[] allowedOrigins,
        boolean allowCredentials,
        boolean allowPrivateNetwork,
        @DefaultValue("PT2H")
        Duration maxAge,
        @DefaultValue({"Content-Type", "Authorization"})
        String[] exposedHeaders
) {
}

