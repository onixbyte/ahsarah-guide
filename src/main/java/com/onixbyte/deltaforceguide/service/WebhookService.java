package com.onixbyte.deltaforceguide.service;

import com.onixbyte.deltaforceguide.manager.WebhookManager;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private final WebhookManager webhookManager;

    public WebhookService(WebhookManager webhookManager) {
        this.webhookManager = webhookManager;
    }
}
