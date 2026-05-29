package com.onixbyte.deltaforceguide.domain.dto;

import java.util.List;

public record GitHubWebhookIssue(
        String url,
        Long id,
        String title,
        String body,
        List<GitHubWebhookLabel> labels
) {

}
