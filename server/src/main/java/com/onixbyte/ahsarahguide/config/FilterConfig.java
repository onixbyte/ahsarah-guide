package com.onixbyte.ahsarahguide.config;

import com.onixbyte.ahsarahguide.filter.WebhookFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<WebhookFilter> webhookFilterBean(WebhookFilter webhookFilter) {
        var registrationBean = new FilterRegistrationBean<WebhookFilter>();

        registrationBean.setFilter(webhookFilter);
        registrationBean.addUrlPatterns("/webhooks/*");
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
