package com.onixbyte.deltaforceguide.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.onixbyte.deltaforceguide.repository"})
public class SpringDataConfig {
}

