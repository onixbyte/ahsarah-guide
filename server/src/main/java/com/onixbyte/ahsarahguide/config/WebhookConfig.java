package com.onixbyte.ahsarahguide.config;

import com.onixbyte.ahsarahguide.properties.GitHubWebhookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({GitHubWebhookProperties.class})
public class WebhookConfig {
}
