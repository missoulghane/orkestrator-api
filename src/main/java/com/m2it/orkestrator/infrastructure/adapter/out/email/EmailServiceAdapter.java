package com.m2it.orkestrator.infrastructure.adapter.out.email;

import com.m2it.orkestrator.domain.port.out.EmailServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Adaptateur pour l'envoi d'emails.
 *
 * Note: Cette implémentation est un stub qui log les emails.
 * Pour une implémentation réelle, intégrer Spring Mail ou un service
 * comme SendGrid, Mailgun, etc.
 */
@Slf4j
@Service
public class EmailServiceAdapter implements EmailServicePort {

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public void sendVerificationEmail(String to, String token) {
        String verificationLink = baseUrl + "/api/auth/verify-email?token=" + token;

        log.info("=== EMAIL DE VERIFICATION ===");
        log.info("Destinataire: {}", to);
        log.info("Lien de vérification: {}", verificationLink);
        log.info("=============================");

        // TODO: Implémenter l'envoi réel d'email
        // Exemple avec Spring Mail:
        // MimeMessage message = mailSender.createMimeMessage();
        // MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // helper.setTo(to);
        // helper.setSubject("Vérification de votre adresse email");
        // helper.setText(buildVerificationEmailContent(verificationLink), true);
        // mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = baseUrl + "/reset-password?token=" + token;

        log.info("=== EMAIL DE REINITIALISATION ===");
        log.info("Destinataire: {}", to);
        log.info("Lien de réinitialisation: {}", resetLink);
        log.info("=================================");

        // TODO: Implémenter l'envoi réel d'email
    }

}
