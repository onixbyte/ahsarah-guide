package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties({CorsProperties.class})
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            CorsProperties properties
    ) {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(properties.allowCredentials());
        corsConfiguration.setAllowedOrigins(List.of(properties.allowedOrigins()));
        corsConfiguration.setAllowedHeaders(List.of(properties.allowedHeaders()));
        corsConfiguration.setAllowedMethods(Stream.of(properties.allowedMethods())
                .map(HttpMethod::name)
                .toList());
        corsConfiguration.setMaxAge(properties.maxAge());
        corsConfiguration.setAllowPrivateNetwork(properties.allowPrivateNetwork());
        corsConfiguration.setExposedHeaders(List.of(properties.exposedHeaders()));

        var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return corsConfigurationSource;
    }
}
