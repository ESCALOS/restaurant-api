package com.nanoka.restaurant_api.email.services;

import com.nanoka.restaurant_api.email.ports.input.EmailServicePort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements EmailServicePort {

    @Value("${front-domain}")
    private String frontDomain;

    @Autowired
    private JavaMailSender mailSender;

    @Async
    @Override
    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
    // Plantilla HTML con marcador de posición
        String htmlTemplate = """
        <!DOCTYPE html>
        <html>
        <head></head>
        <body>
            <p>Hola,</p>
            <p>Hemos recibido tu solicitud para restablecer tu contraseña. Haz clic en el botón de abajo:</p>
            <a href="{{FRONT_DOMAIN}}/reset-password?token={{TOKEN}}" style="display:inline-block; padding: 10px 20px; color: white; background-color: #d2c087; text-decoration: none; border-radius: 5px;">
                Restablecer contraseña
            </a>
            <p>Si no solicitaste este cambio, ignora este correo.</p>
        </body>
        </html>
        """;

        // Reemplazar los marcadores
        String htmlContent = htmlTemplate
                .replace("{{FRONT_DOMAIN}}", frontDomain)
                .replace("{{TOKEN}}", token);

        // Crear mensaje de correo
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("Restablecimiento de contraseña");
        helper.setText(htmlContent, true); // true para contenido HTML

        // Enviar correo
        mailSender.send(message);
    }
}
