package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.filter.WebhookFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<WebhookFilter> loggingFilter(WebhookFilter webhookFilter) {
        var registrationBean = new FilterRegistrationBean<WebhookFilter>();

        registrationBean.setFilter(webhookFilter);
        registrationBean.addUrlPatterns("/webhooks/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
