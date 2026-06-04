package com.onixbyte.deltaforceguide.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Delta Force Guide Server",
                description = "API for managing Delta Force game firearm builds",
                version = "1.3.4",
                contact = @Contact(
                        name = "Zihlu Wang",
                        email = "zihlu.wang@onixbyte.com"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://git.onixbyte.cn/onixbyte/delta-force-guide-server/-/raw/main/LICENCE"
                )
        )
)
@Configuration
public class OpenApiConfiguration {
}
