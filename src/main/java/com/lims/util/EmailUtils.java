package com.lims.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility to send transactional emails (e.g. Password Reset, Purchase Order notifications).
 */
public class EmailUtils {

    private static final Logger LOGGER = Logger.getLogger(EmailUtils.class.getName());

    public static boolean sendHtmlMail(String recipient, String subject, String htmlContent) {
        String host = "smtp.gmail.com";
        String port = "587";
        final String username = System.getenv("SMTP_USERNAME");
        final String password = System.getenv("SMTP_PASSWORD");

        if (username == null || password == null) {
            LOGGER.log(Level.INFO, "SMTP credentials not configured. Mocking email delivery to {0} with subject: {1}",
                    new Object[]{recipient, subject});
            return true; // Simulation for development
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "LIMS Notification"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            LOGGER.info("Email sent successfully to " + recipient);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send email to " + recipient, e);
            return false;
        }
    }
}

