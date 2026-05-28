package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.properties.GitLabWebhookProperties;
import com.onixbyte.deltaforceguide.properties.WebhookProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookManager {

    private final WebhookProperties webhookProperties;

    @Autowired
    public WebhookManager(WebhookProperties webhookProperties) {
        this.webhookProperties = webhookProperties;
    }

    public GitLabWebhookProperties getGitLabWebhookProperties() {
        return webhookProperties.gitlab();
    }
}
