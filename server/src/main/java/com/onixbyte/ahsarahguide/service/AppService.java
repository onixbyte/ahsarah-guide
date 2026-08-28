package com.onixbyte.ahsarahguide.service;

import com.onixbyte.ahsarahguide.manager.AppManager;
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
