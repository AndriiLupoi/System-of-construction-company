package com.example.My_Course_Project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationCode(String email, String verificationCode) {
        // Створюємо нове повідомлення
        MimeMessage message = mailSender.createMimeMessage();
        try {
            // Використовуємо допоміжний об'єкт для налаштування повідомлення
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no-reply@mycourseproject.com");
            helper.setTo(email);
            helper.setSubject("Код підтвердження");

            // Вміст повідомлення
            String htmlContent = "<h1>Код підтвердження</h1>"
                    + "<p>Ваш код підтвердження: <strong>" + verificationCode + "</strong></p>"
                    + "<p>Введіть цей код на сторінці зміни паролю.</p>";

            helper.setText(htmlContent, true); // true вказує, що це HTML контент

            // Відправляємо листа
            mailSender.send(message);

            System.out.println("Verification email sent successfully to: " + email);

        } catch (MessagingException e) {
            System.out.println("Error while sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
