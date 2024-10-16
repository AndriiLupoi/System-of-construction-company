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

            String htmlContent = "<html lang=\"uk\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "            color: #333;\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #4CAF50;\n" +
                    "        }\n" +
                    "        p {\n" +
                    "            font-size: 16px;\n" +
                    "            line-height: 1.5;\n" +
                    "        }\n" +
                    "        .code {\n" +
                    "            display: inline-block;\n" +
                    "            background-color: #e7f3fe;\n" +
                    "            border: 1px solid #2196F3;\n" +
                    "            color: #0c5460;\n" +
                    "            padding: 10px;\n" +
                    "            border-radius: 4px;\n" +
                    "            font-weight: bold;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            margin-top: 20px;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #777;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <h1>Код підтвердження</h1>\n" +
                    "    <p>Ваш код підтвердження:</p>\n" +
                    "    <p class=\"code\">" + verificationCode + "</p>\n" +
                    "    <p>Введіть цей код на сторінці зміни паролю.</p>\n" +
                    "    <div class=\"footer\">\n" +
                    "        <p>Дякуємо за використання нашого сервісу!</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";


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
