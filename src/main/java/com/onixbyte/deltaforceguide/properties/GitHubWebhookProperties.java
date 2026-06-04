package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.webhook.github")
public record GitHubWebhookProperties(
        String secret,
        List<String> allowedUsers
) {
}
