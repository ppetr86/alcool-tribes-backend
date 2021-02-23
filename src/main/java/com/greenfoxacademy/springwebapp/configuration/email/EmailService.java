package com.greenfoxacademy.springwebapp.configuration.email;

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

  public void sendReigstrationMail(String to, String username, String kingdomName) {
    StringBuilder mailBody = new StringBuilder();
    mailBody.append("Welcome ").append(username).append("!\n");
    mailBody.append(kingdomName).append(" is ready! You just need to confirm your email address\n");
    mailBody.append("and then you are ready to conquer the world :)\n");
    mailBody.append(" Please confirm your email address by opening the following url: \n");
    mailBody.append(emailConfig.getValidationUrl()).append(username).append("\n\n");
    mailBody.append("Confirm Email Address\n\n");
    mailBody.append(" — The Tribes Team\n");

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Confirm Email Alcool Game");
    message.setText(mailBody.toString());
    mailSender.send(message);
  }

  public void sendRegistrationEmailAsHTML(String to, String username, String kingdomname) throws MessagingException {
    Context context = new Context();
    context.setVariable("username", username);
    context.setVariable("kingdomname", kingdomname);
    context.setVariable("url", emailConfig.getValidationUrl());

    String process = templateEngine.process("reg_email", context);
    javax.mail.internet.MimeMessage mimeMessage = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
    helper.setSubject("Confirm Email Alcool Game");
    helper.setText(process, true);
    helper.setTo(to);
    mailSender.send(mimeMessage);
  }

  public void sendSimpleMessage(String to, String username, String kingdomname) throws MessagingException, IOException {
    Context context = new Context();
    context.setVariable("username", username);
    context.setVariable("kingdomname", kingdomname);
    context.setVariable("url", emailConfig.getValidationUrl());

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());

    String html = templateEngine.process("reg_email", context);

    helper.setTo(to);
    helper.setText(html, true);
    helper.setSubject("Confirm Email Alcool Game");
    helper.setFrom(emailConfig.getUsername());

    mailSender.send(message);
  }
}