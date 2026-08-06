package com.onixbyte.deltaforceguide.interceptor;

import com.onixbyte.crypto.util.CryptoUtil;
import com.onixbyte.deltaforceguide.exeption.InternalServerErrorException;
import com.onixbyte.deltaforceguide.exeption.UnauthorisedException;
import com.onixbyte.deltaforceguide.manager.WebhookManager;
import com.onixbyte.deltaforceguide.shared.GitHubWebhookHeader;
import com.onixbyte.deltaforceguide.wrapper.RepeatedlyReadRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Verifies GitHub webhook requests by validating the {@code X-Hub-Signature-256}
 * header against the configured secret using HMAC-SHA256.
 *
 * <p>Verification is skipped when no secret is configured. The signature format is
 * {@code sha256=<hex-digest>} as documented by GitHub.
 */
@Component
public class GitHubWebhookInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GitHubWebhookInterceptor.class);

    private final WebhookManager webhookManager;

    public GitHubWebhookInterceptor(WebhookManager webhookManager) {
        this.webhookManager = webhookManager;
    }

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (!(request instanceof RepeatedlyReadRequestWrapper req)) {
            throw new InternalServerErrorException(
                    "Request body is not readable");
        }

        var secret = webhookManager.secret();
        if (secret == null || secret.isBlank()) {
            log.debug("No GitHub webhook secret configured, skipping signature verification");
            return true;
        }

        var signatureHeader = req.getHeader(GitHubWebhookHeader.SIGNATURE_256);
        if (signatureHeader == null || signatureHeader.isBlank()) {
            log.warn("Missing {} header from ip={}",
                    GitHubWebhookHeader.SIGNATURE_256, request.getRemoteAddr());
            throw new UnauthorisedException(
                    "Missing webhook signature header");
        }

        var body = req.getBodyString();
        try {
            var computed = "sha256=" + CryptoUtil.hmacSha256(body, secret);

            if (!MessageDigest.isEqual(
                    computed.getBytes(StandardCharsets.UTF_8),
                    signatureHeader.getBytes(StandardCharsets.UTF_8))) {
                log.warn("Invalid webhook signature from ip={}", request.getRemoteAddr());
                throw new UnauthorisedException("Invalid webhook signature");
            }

            return true;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Failed to compute HMAC-SHA256", e);
            throw new InternalServerErrorException(
                    "Failed to verify webhook signature");
        }
    }
}
