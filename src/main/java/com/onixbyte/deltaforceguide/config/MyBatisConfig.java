package com.onixbyte.deltaforceguide.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.onixbyte.deltaforceguide.mapper"})
public class MyBatisConfig {
}

