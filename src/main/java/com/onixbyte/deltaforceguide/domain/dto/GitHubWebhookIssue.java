package com.onixbyte.deltaforceguide.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubWebhookIssue(
        String url,
        Long id,
        Long number,
        String title,
        String body,
        List<GitHubWebhookLabel> labels
) {

}
