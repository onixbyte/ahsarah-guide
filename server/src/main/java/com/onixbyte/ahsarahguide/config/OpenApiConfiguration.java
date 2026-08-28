package com.onixbyte.ahsarahguide.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Ahsarah Guide Server",
                description = "API for Ahsarah Guide",
                version = "1.4.0",
                contact = @Contact(
                        name = "Zihlu Wang",
                        email = "zihlu.wang@onixbyte.com"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://onixbyte.dev/onixbyte/ahsarah-guide/raw/branch/main/LICENCE"
                )
        )
)
@Configuration
public class OpenApiConfiguration {
}
