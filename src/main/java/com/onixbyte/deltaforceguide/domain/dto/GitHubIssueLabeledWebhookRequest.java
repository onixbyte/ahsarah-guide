package com.onixbyte.deltaforceguide.domain.dto;

public record GitHubIssueLabeledWebhookRequest(
        String action,
        GitHubWebhookIssue issue
) {
}
