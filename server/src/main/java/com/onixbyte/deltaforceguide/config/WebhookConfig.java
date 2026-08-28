package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.properties.GitHubWebhookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({GitHubWebhookProperties.class})
public class WebhookConfig {
}
