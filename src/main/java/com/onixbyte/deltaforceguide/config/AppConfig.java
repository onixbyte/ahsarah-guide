package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.interceptor.GitLabWebhookInterceptor;
import com.onixbyte.deltaforceguide.properties.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig implements WebMvcConfigurer {

    private final GitLabWebhookInterceptor gitLabWebhookInterceptor;

    @Autowired
    public AppConfig(
            GitLabWebhookInterceptor gitLabWebhookInterceptor
    ) {
        this.gitLabWebhookInterceptor = gitLabWebhookInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(gitLabWebhookInterceptor)
                .addPathPatterns("/webhook/gitlab");
    }
}
