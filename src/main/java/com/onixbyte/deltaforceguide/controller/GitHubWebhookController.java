package com.onixbyte.deltaforceguide.controller;

import com.onixbyte.deltaforceguide.domain.dto.GitHubIssueRequest;
import com.onixbyte.deltaforceguide.service.WebhookService;
import com.onixbyte.deltaforceguide.shared.GitHubWebhookHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/github")
public class GitHubWebhookController {

    private static final Logger log = LoggerFactory.getLogger(GitHubWebhookController.class);

    private final WebhookService webhookService;

    public GitHubWebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(GitHubWebhookHeader.EVENT) String event,
            @RequestBody GitHubIssueRequest request
    ) {
        if (!"issues".equals(event)) {
            log.debug("Ignoring non-issue event: {}", event);
            return ResponseEntity.ok().build();
        }
        if (!"opened".equals(request.action())) {
            log.debug("Ignoring issue action: {}", request.action());
            return ResponseEntity.ok().build();
        }
        webhookService.processIssueEvent(request);
        return ResponseEntity.ok().build();
    }
}
