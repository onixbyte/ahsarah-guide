package com.onixbyte.deltaforceguide.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubIssueRequest(
        String action,
        GitHubWebhookIssue issue,
        GitHubWebhookRepository repository,
        GitHubWebhookSender sender
) {
}
