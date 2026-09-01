package com.onixbyte.ahsarahguide.manager;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

@Component
public class AppManager {

    private final BuildProperties buildProperties;

    public AppManager(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    /**
     * Retrieves the application version.
     *
     * @return the version string of this application
     */
    public String getVersion() {
        return "v%s-%s by @%s".formatted(
                buildProperties.getVersion(),
                buildProperties.get("channel"),
                buildProperties.get("vendor")
        );
    }
}
