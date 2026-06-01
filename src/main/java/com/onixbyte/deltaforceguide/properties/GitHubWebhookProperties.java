package com.onixbyte.deltaforceguide.properties;

import java.util.List;

public record GitHubWebhookProperties(
        String secret,
        List<String> allowedUsers
) {
}
