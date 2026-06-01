package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.interceptor.GitHubWebhookInterceptor;
import com.onixbyte.deltaforceguide.interceptor.TrafficInterceptor;
import com.onixbyte.deltaforceguide.properties.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig implements WebMvcConfigurer {

    private final TrafficInterceptor trafficInterceptor;

    private final GitHubWebhookInterceptor gitHubWebhookInterceptor;

    @Autowired
    public AppConfig(
            TrafficInterceptor trafficInterceptor,
            GitHubWebhookInterceptor gitHubWebhookInterceptor
    ) {
        this.trafficInterceptor = trafficInterceptor;
        this.gitHubWebhookInterceptor = gitHubWebhookInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(trafficInterceptor);
        registry.addInterceptor(gitHubWebhookInterceptor)
                .addPathPatterns("/webhooks/github");
    }
}
