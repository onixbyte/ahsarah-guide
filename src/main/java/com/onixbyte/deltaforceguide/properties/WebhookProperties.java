package com.onixbyte.deltaforceguide.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.webhook")
public record WebhookProperties(
        GitLabWebhookProperties gitlab
) {
}
