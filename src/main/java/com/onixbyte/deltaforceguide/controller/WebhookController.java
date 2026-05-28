package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.GitLabWebhookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/gitlab")
    public void gitlabWebhook(
            @RequestBody GitLabWebhookRequest request
    ) {
        log.info("request={}", request);
    }
}
