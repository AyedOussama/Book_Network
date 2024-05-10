package com.ayed.booknetwork.Email;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;


@Service // lombok annotation for logging in the class and it will create a logger object for us (Error, Warn, Info, Debug)
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async // we use the @Async annotation to make the method run in a separate thread
    public void sendEmail(
            String to,
            String username,
            EmailTemplateName emailTemplate,
            String confirmationUrl,// we pass the confirmation URL to the email template to be sent to the user
            String activationCode,// we pass the activation code to the email template to be sent to the user
            String subject // the subject of the email
     ) throws MessagingException

    {
        String templateName;
        if (emailTemplate == null) {
            templateName = "confirm-email";
        } else {
            templateName = emailTemplate.name();
        }
        MimeMessage mimeMessage = mailSender.createMimeMessage(); // we create a mime message object to send the email
        MimeMessageHelper helper = new MimeMessageHelper( // we create a mime message helper object to help us set the properties of the email
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        // to pass the properties to the  html email template we create a map object
        Map<String, Object> properties = new HashMap<>(); // we create a map to store the properties that we want to send to the email template
        properties.put("username", username);
        properties.put("confirmationUrl", confirmationUrl);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        // we create a context object to store the properties to be sent to the email template because we sent the email template as a string
        // we use context to replace the variables in the email template with the values we want to send
        context.setVariables(properties);

        helper.setFrom("ayedoussama2002@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);


        // how thymeleaf and spring  will process the email template and replace the variables with the values we want to send
        String template = templateEngine.process(templateName, context);// we process the email template with the context object

        helper.setText(template, true);

        mailSender.send(mimeMessage);// we send the email (mime message is the email object that we send)
    }
}
