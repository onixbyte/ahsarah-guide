package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.properties.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;
import java.util.stream.Stream;

@Configuration
@EnableConfigurationProperties({CorsProperties.class})
public class CorsConfig implements WebMvcConfigurer {

    private final CorsProperties properties;

    public CorsConfig(CorsProperties properties) {
        this.properties = properties;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(toSafeArray(properties.allowedOrigins()))
                .allowedHeaders(toSafeArray(properties.allowedHeaders()))
                .allowedMethods(toHttpMethodNames(properties.allowedMethods()))
                .allowCredentials(properties.allowCredentials())
                .maxAge(properties.maxAge().toSeconds())
                .exposedHeaders(toSafeArray(properties.exposedHeaders()));
    }

    private static String[] toSafeArray(String[] values) {
        return values == null ? new String[0] : values;
    }

    private static String[] toHttpMethodNames(HttpMethod[] methods) {
        return Optional.ofNullable(methods)
                .stream()
                .flatMap(Stream::of)
                .map(HttpMethod::name)
                .toList()
                .toArray(String[]::new);
    }

}
