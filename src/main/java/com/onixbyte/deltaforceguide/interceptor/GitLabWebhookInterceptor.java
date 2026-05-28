package com.onixbyte.deltaforceguide.interceptor;

import com.onixbyte.deltaforceguide.exeption.BizException;
import com.onixbyte.deltaforceguide.properties.WebhookProperties;
import com.onixbyte.deltaforceguide.wrapper.RepeatedlyReadRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * Verifies GitLab webhook requests by validating the {@code webhook-id},
 * {@code webhook-timestamp}, and {@code webhook-signature} headers against
 * a configured signing token using HMAC-SHA256.
 *
 * <p>Supports GitLab's v1 signature scheme. Verification is skipped when no
 * signing token is configured.
 */
@Component
public class GitLabWebhookInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GitLabWebhookInterceptor.class);
    private static final String TOKEN_PREFIX = "whsec_";

    private final WebhookProperties webhookProperties;

    /**
     * Creates a new interceptor with the given webhook configuration.
     *
     * @param webhookProperties the webhook configuration properties
     */
    public GitLabWebhookInterceptor(WebhookProperties webhookProperties) {
        this.webhookProperties = webhookProperties;
    }

    /**
     * Validates the GitLab webhook signature headers on the incoming request.
     * Reads {@code webhook-id}, {@code webhook-timestamp}, and
     * {@code webhook-signature} headers and verifies the signature against the
     * configured signing token. If no token is configured, verification is skipped.
     *
     * @param request  the incoming HTTP request (must be a {@link RepeatedlyReadRequestWrapper})
     * @param response the HTTP response
     * @param handler  the chosen handler to execute
     * @return {@code true} if the request is authentic
     * @throws BizException with {@code 401} if headers are missing or the signature is invalid,
     *                      with {@code 500} if verification fails unexpectedly
     */
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (!(request instanceof RepeatedlyReadRequestWrapper req)) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Request body is not readable");
        }

        var webhookId = request.getHeader("webhook-id");
        var webhookTimestamp = request.getHeader("webhook-timestamp");
        var webhookSignature = request.getHeader("webhook-signature");

        if (webhookId == null || webhookTimestamp == null || webhookSignature == null) {
            log.warn("Missing webhook headers from ip={}", request.getRemoteAddr());
            throw new BizException(HttpStatus.UNAUTHORIZED,
                    "Missing webhook verification headers");
        }

        var signingToken = webhookProperties.gitlab().signingToken();
        if (signingToken == null || signingToken.isBlank()) {
            log.debug("No GitLab signing token configured, skipping signature verification");
            return true;
        }

        var body = req.getBodyString();
        var signedContent = "%s.%s.%s".formatted(webhookId, webhookTimestamp, body);

        try {
            var decodedKey = decodeSigningToken(signingToken);
            var computedDigest = computeHmacSha256(decodedKey, signedContent);
            var computedSignature = "v1,%s".formatted(computedDigest);

            var signatures = webhookSignature.split(" ");
            var matched = false;
            for (var sig : signatures) {
                if (MessageDigest.isEqual(computedSignature.getBytes(StandardCharsets.UTF_8),
                        sig.trim().getBytes(StandardCharsets.UTF_8))) {
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                log.warn("Invalid webhook signature from ip={}", request.getRemoteAddr());
                throw new BizException(HttpStatus.UNAUTHORIZED, "Invalid webhook signature");
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to verify webhook signature", e);
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to verify webhook signature");
        }

        return true;
    }

    /**
     * Decodes a GitLab-format signing token by stripping the {@code whsec_} prefix
     * and Base64-decoding the remainder.
     *
     * @param token the prefixed signing token
     * @return the raw key bytes
     * @throws BizException if the token does not start with {@code whsec_}
     */
    private byte[] decodeSigningToken(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new BizException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Signing token must start with " + TOKEN_PREFIX);
        }
        var encoded = token.substring(TOKEN_PREFIX.length());
        return Base64.getDecoder().decode(encoded);
    }

    /**
     * Computes the Base64-encoded HMAC-SHA256 digest for the given data.
     *
     * @param key  the secret key bytes
     * @param data the content to sign
     * @return Base64-encoded HMAC-SHA256 digest
     * @throws Exception if the HMAC algorithm is unavailable
     */
    private String computeHmacSha256(byte[] key, String data) throws Exception {
        var mac = Mac.getInstance("HmacSHA256");
        var secretKey = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKey);
        var hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}
