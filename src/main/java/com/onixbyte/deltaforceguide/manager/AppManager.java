package com.onixbyte.deltaforceguide.manager;

import com.onixbyte.deltaforceguide.properties.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class AppManager {

    private final AppProperties appProperties;

    public AppManager(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Retrieves the application version.
     *
     * @return the version string of this application
     */
    public String getVersion() {
        return appProperties.version();
    }
}
