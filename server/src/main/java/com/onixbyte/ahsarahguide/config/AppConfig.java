package com.onixbyte.ahsarahguide.config;

import com.onixbyte.ahsarahguide.interceptor.GitHubWebhookInterceptor;
import com.onixbyte.ahsarahguide.interceptor.TrafficInterceptor;
import com.onixbyte.ahsarahguide.security.resolver.CurrentUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    private final TrafficInterceptor trafficInterceptor;

    private final GitHubWebhookInterceptor gitHubWebhookInterceptor;

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    @Autowired
    public AppConfig(
            TrafficInterceptor trafficInterceptor,
            GitHubWebhookInterceptor gitHubWebhookInterceptor,
            CurrentUserArgumentResolver currentUserArgumentResolver
    ) {
        this.trafficInterceptor = trafficInterceptor;
        this.gitHubWebhookInterceptor = gitHubWebhookInterceptor;
        this.currentUserArgumentResolver = currentUserArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(trafficInterceptor);
        registry.addInterceptor(gitHubWebhookInterceptor)
                .addPathPatterns("/webhooks/github");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserArgumentResolver);
    }
}
