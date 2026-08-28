package com.onixbyte.ahsarahguide.shared;

/**
 * This class lists the header names that GitHub sends in webhook requests.
 *
 * @author siujamo
 */
public class GitHubWebhookHeader {

    /**
     * The unique identifier of the webhook.
     */
    public static final String HOOK_ID = "X-GitHub-Hook-ID";

    /**
     * The name of the event that triggered the delivery.
     */
    public static final String EVENT = "X-GitHub-Event";

    /**
     * A globally unique identifier (GUID) to identify the event.
     */
    public static final String DELIVERY = "X-GitHub-Delivery";

    /**
     * This header is sent if the webhook is configured with a {@code secret}. This is the HMAC hex
     * digest of the request body, and is generated using the SHA-1 hash function and the secret as
     * the HMAC {@code key}. {@code X-Hub-Signature} is provided for compatibility with
     * existing integrations. We recommend that you use the more secure
     * {@code X-Hub-Signature-256} instead.
     */
    public static final String SIGNATURE = "X-Hub-Signature";

    /**
     * This header is sent if the webhook is configured with a {@code secret}. This is the HMAC hex
     * digest of the request body, and is generated using the SHA-256 hash function and the
     * {@code secret} as the HMAC key. For more information, see <a href="https://docs.github.com/en/webhooks/using-webhooks/securing-your-webhooks"
     * >Validating webhook deliveries</a>.
     */
    public static final String SIGNATURE_256 = "X-Hub-Signature-256";

    /**
     * This header will always have the prefix {@code GitHub-Hookshot/}.
     */
    public static final String USER_AGENT = "User-Agent";

    /**
     * The type of resource where the webhook was created.
     */
    public static final String INSTALLATION_TARGET_TYPE = "X-GitHub-Hook-Installation-Target-Type";

    /**
     * The unique identifier of the resource where the webhook was created.
     */
    public static final String INSTALLATION_TARGET_ID = "X-GitHub-Hook-Installation-Target-ID";
}
