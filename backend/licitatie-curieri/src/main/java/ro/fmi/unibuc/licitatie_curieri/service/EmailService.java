package ro.fmi.unibuc.licitatie_curieri.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Metoda pentru trimiterea codului de verificare
    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email); // Adresa de email destinatar
        message.setSubject("Your Verification Code"); // Subiectul email-ului
        message.setText("Your verification code is: " + code); // Textul email-ului, codul de verificare

        mailSender.send(message); // Trimite email-ul
    }
}
