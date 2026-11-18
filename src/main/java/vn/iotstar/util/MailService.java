package vn.iotstar.util;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public final class MailService {
    private MailService() {
    }

    public static void sendOtp(String recipient, String purpose, String otp) {
    	String host = required("MAIL_HOST");
    	String username = required("MAIL_USERNAME");
        String password = required("MAIL_PASSWORD");
    	String from = env("MAIL_FROM", username);
    	String port = env("MAIL_PORT", "587");

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient, false));
            message.setSubject("Mã OTP " + purpose, StandardCharsets.UTF_8.name());
            message.setText("Mã OTP của bạn là: " + otp
                    + "\nMã có hiệu lực trong 10 phút. Nếu bạn không yêu cầu mã này, hãy bỏ qua email.",
                    StandardCharsets.UTF_8.name());
            Transport.send(message);
        } catch (Exception e) {
            throw new IllegalStateException("Không gửi được email OTP.", e);
        }
    }

    private static String required(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Thiếu cấu hình môi trường " + name + ".");
        }
        return value;
    }

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
