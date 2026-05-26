package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.common")
public record AppProperties(
        String version
) {
}
