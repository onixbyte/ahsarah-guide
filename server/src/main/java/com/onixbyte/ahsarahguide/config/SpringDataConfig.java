package com.onixbyte.ahsarahguide.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuration for Spring Data JPA auditing and repository settings.
 *
 * @author zihluwang
 */
@Configuration
@EnableJpaRepositories(basePackages = {"com.onixbyte.ahsarahguide.repository"})
public class SpringDataConfig {
}

