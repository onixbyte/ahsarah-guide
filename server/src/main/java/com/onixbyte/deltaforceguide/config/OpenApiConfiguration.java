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
                version = "1.4.0",
                contact = @Contact(
                        name = "Zihlu Wang",
                        email = "zihlu.wang@onixbyte.com"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://onixbyte.dev/onixbyte/delta-force-guide-server/raw/branch/main/LICENCE"
                )
        )
)
@Configuration
public class OpenApiConfiguration {
}
