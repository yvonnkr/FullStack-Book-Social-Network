package com.yvolabs.book.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 28/07/2024
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${application.mailing.from-email}")
    private String fromEmail;

    @Async
    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplate,
                          String confirmationUrl,
                          String activationCode,
                          String subject) throws MessagingException {

        String templateName = emailTemplate == null ? "confirm-email" : emailTemplate.name();

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(properties);

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);

        try {
            log.info("Sending email to: {}", to);
            String template = templateEngine.process(templateName, thymeleafContext);
            helper.setText(template, true);
            mailSender.send(mimeMessage);
            log.info("Email sent to: {}", to);
        } catch (Exception e) {
            log.info("Error sending email to: {}", e.getMessage());
        }

    }
}
