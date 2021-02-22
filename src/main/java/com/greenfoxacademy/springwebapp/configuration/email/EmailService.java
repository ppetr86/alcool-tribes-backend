package com.greenfoxacademy.springwebapp.configuration.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service("emailService")
@AllArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final EmailConfig emailConfig;

  public void sendReigstrationMail(String to, String username, String kingdomName) {
    StringBuilder mailBody = new StringBuilder();
    mailBody.append("Welcome ").append(username).append("!\n");
    mailBody.append(kingdomName).append(" is ready! You just need to confirm your email address\n");
    mailBody.append("and then you are ready to conquer the world :)");
    mailBody.append(" Please confirm your email address by opening the following url: \n");
    mailBody.append(emailConfig.getValidationUrl()).append(username).append("\n");
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
}