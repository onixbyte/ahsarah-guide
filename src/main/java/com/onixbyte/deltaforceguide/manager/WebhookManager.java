package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.properties.GitHubWebhookProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebhookManager {

    private final GitHubWebhookProperties gitHubWebhookProperties;

    public WebhookManager(GitHubWebhookProperties gitHubWebhookProperties) {
        this.gitHubWebhookProperties = gitHubWebhookProperties;
    }

    public String secret() {
        return gitHubWebhookProperties.secret();
    }

    public List<String> allowedUsers() {
        return gitHubWebhookProperties.allowedUsers();
    }
}
