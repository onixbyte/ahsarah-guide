package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.properties.WebhookProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebhookProperties.class)
public class WebhookConfig {
}
