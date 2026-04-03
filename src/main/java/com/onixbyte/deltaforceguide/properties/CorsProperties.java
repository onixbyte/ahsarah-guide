package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.HttpMethod;

import java.time.Duration;

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

