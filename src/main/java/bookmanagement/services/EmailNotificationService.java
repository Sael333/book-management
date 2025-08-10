package bookmanagement.services;

import bookmanagement.dto.EmailMessageDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;


@Slf4j
@Service
public class EmailNotificationService {

    private static final String SUBJECT = "Andalusian rooms security code";

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Value("${application.email.smtp.user}")
    private String user;

    public void sendEmail(EmailMessageDto emailMessageDto, String template) {
        try {
            // Crear el mensaje MIME
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariable("emailMessageDto", emailMessageDto);
            String htmlContent = templateEngine.process(template, context);

            // Configurar el mensaje
            helper.setFrom(user);
            helper.setTo(emailMessageDto.getTo());
            helper.setSubject(SUBJECT);
            helper.setText(htmlContent, true);

            // Enviar el mensaje
            mailSender.send(mimeMessage);

            log.info("Correo enviado exitosamente!");
        } catch (MessagingException e) {
            log.error("Error durante el envío del mensaje", e);
        }
    }
}