package com.onixbyte.deltaforceguide.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.OffsetDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitLabWebhookRequest(
        String objectKind,
        String eventType,
        GitLabWebhookObjectAttributes objectAttributes
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GitLabWebhookLabel(
            Long id,
            String title,
            @JsonProperty("color")
            String colour,
            Long projectId,
            String createdAt,
            String updatedAt,
            Boolean template,
            String description,
            String type,
            Long groupId
    ) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record GitLabWebhookObjectAttributes(
            Long id,
            String title,
            String description,
            List<GitLabWebhookLabel> labels
    ) {}


}
