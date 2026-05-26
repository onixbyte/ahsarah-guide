package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.manager.AppManager;
import org.springframework.stereotype.Service;

@Service
public class AppService {

    private final AppManager appManager;

    public AppService(AppManager appManager) {
        this.appManager = appManager;
    }

    public String getVersion() {
        return appManager.getVersion();
    }
}
