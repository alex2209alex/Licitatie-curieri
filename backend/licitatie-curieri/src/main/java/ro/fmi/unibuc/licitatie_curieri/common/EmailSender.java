package ro.fmi.unibuc.licitatie_curieri.common;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String recipientEmail, String code, String subject, String text) throws MessagingException {
        String from = "";
        String pass = "";
        String host = "smtp.gmail.com";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject(subject);
        message.setText(text);

        // Trimiterea mesajului
        Transport.send(message);
        System.out.println("Sent message successfully....");
    }

    public static String generateCode() {
        return Integer.toString((int) (Math.random() * 900000) + 100000);
    }
}
