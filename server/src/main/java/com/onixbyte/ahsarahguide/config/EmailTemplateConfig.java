package com.onixbyte.ahsarahguide.config;

import com.onixbyte.captcha.text.TextProducer;
import com.onixbyte.captcha.text.impl.DefaultTextProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.StandardCharsets;

@Configuration
public class EmailTemplateConfig {

    @Bean
    public TemplateEngine emailTemplateEngine() {
        var resolver = new ClassLoaderTemplateResolver();
        // Template files are located under src/main/resources/mail-templates/
        resolver.setPrefix("mail-templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setCacheable(true); // Enable caching for the production environment

        var templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(resolver);
        return templateEngine;
    }

    @Bean
    public TextProducer textProducer() {
        return DefaultTextProducer.builder().build();
    }
}
