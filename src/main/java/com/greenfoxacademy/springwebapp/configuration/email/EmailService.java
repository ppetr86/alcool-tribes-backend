package com.greenfoxacademy.springwebapp.configuration.email;

import com.greenfoxacademy.springwebapp.configuration.email.context.AbstractEmailContext;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service("emailService")
@AllArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;
  private final EmailConfig emailConfig;

  public void sendTextEmail(AbstractEmailContext email, String to, String username, String kingdomName)throws MessagingException {
    StringBuilder mailBody = new StringBuilder();
    mailBody.append("Welcome ").append(username).append("!\n\n");
    mailBody.append(kingdomName).append(" is ready! You just need to confirm your email address\n");
    mailBody.append("and then you are ready to conquer the world :)\n");
    mailBody.append(" Please confirm your email address by opening the following url: \n");
    mailBody.append(email.getContext().get("verificationURL")).append("\n\n");
    mailBody.append("Confirm Email Address\n\n");
    mailBody.append(" — The Tribes Team\n");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Confirm Email Alcool Game");
    message.setText(mailBody.toString());
    mailSender.send(message);
  }

  public void sendMail(AbstractEmailContext email, String to, String username, String kingdomname) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(email.getContext());

    String emailContent = templateEngine.process(email.getTemplateLocation(), context);

    mimeMessageHelper.setTo(to);
    mimeMessageHelper.setSubject("Verify your email for Alcool Game");
    mimeMessageHelper.setFrom("123");
    mimeMessageHelper.setText(emailContent, true);
    mailSender.send(message);
  }
}