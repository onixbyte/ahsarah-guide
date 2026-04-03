package com.onixbyte.deltaforceguide.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.onixbyte.deltaforceguide.shared.JacksonModules;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomiser() {
        return (builder) -> {
            builder.modules(JacksonModules.DATE_TIME_MODULE);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        };
    }
}

