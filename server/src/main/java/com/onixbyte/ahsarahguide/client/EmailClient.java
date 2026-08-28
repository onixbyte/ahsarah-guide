package com.onixbyte.ahsarahguide.client;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class EmailClient {

    private static final Logger log = LoggerFactory.getLogger(EmailClient.class);

    private final JavaMailSender mailSender;

    private final TemplateEngine emailTemplateEngine;

    @Autowired
    public EmailClient(
            JavaMailSender mailSender,
            TemplateEngine emailTemplateEngine
    ) {
        this.mailSender = mailSender;
        this.emailTemplateEngine = emailTemplateEngine;
    }

    private String fromAddress;

    @Autowired
    public void setFromAddress(@Value("${spring.mail.username}") String fromAddress) {
        this.fromAddress = fromAddress;
    }

    /**
     * Send a template-based HTML email asynchronously.
     *
     * @param toRecipient  recipient email address
     * @param subject      subject line
     * @param templateName template name (without .html extension, under mail-templates/)
     * @param variables    context variables required by the template
     */
    @Async
    public void sendTemplateMail(String toRecipient, String subject, String templateName, Map<String, Object> variables) {
        try {
            // Render HTML content using the dedicated email template engine
            var context = new Context();
            if (variables != null && !variables.isEmpty()) {
                context.setVariables(variables);
            }
            var htmlBody = emailTemplateEngine.process(templateName, context);

            log.debug("htmlBody={}", htmlBody);

            // Build the MIME message
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            helper.setFrom(new InternetAddress(fromAddress, "阿萨拉向导", StandardCharsets.UTF_8.name()));
            helper.setTo(toRecipient);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            // Dispatch the message
            mailSender.send(message);
            log.info("Successfully dispatched email to: [{}] with subject: [{}]", toRecipient, subject);

        } catch (MessagingException | UnsupportedEncodingException ex) {
            log.error("Failed to construct or send email to: [{}], subject: [{}]", toRecipient, subject, ex);
            // Handle or rethrow custom business exception if required
        } catch (Exception ex) {
            log.error("Unexpected error occurred while processing email for: [{}]", toRecipient, ex);
        }
    }

    /**
     * Convenience method specifically for sending registration/login verification codes.
     *
     * @param toRecipient   target recipient email
     * @param username      display username
     * @param code          verification code string
     * @param expireMinutes expiration time in minutes
     */
    @Async
    public void sendVerificationCode(String toRecipient, String username, String code, int expireMinutes) {
        var subject = "【阿萨拉向导】账号注册验证码";
        var variables = Map.<String, Object>of(
                "username", username,
                "code", code,
                "expireMinutes", expireMinutes
        );

        sendTemplateMail(toRecipient, subject, "verification-code", variables);
    }
}
