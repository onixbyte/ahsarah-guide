package com.onixbyte.ahsarahguide.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Ahsarah Guide Server",
                description = "API for Ahsarah Guide",
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

    /**
     * Overrides the OpenAPI info version with the packaged build version so the
     * docs always match the released artefact instead of a hard-coded value.
     *
     * @param buildProperties the build-info properties generated at build time
     * @return customiser that stamps the build version onto the API info
     */
    @Bean
    public OpenApiCustomizer versionCustomizer(BuildProperties buildProperties) {
        return openApi -> {
            if (openApi.getInfo() != null) {
                openApi.getInfo().setVersion(buildProperties.getVersion());
            }
        };
    }
}
