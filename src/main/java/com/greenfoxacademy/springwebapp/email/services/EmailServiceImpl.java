package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmailContext;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

@Service("emailService")
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Override
  public void sendTextEmail(AbstractEmailContext email)throws MessagingException {

    System.out.println(email.getContext().get("verificationURL"));

    SimpleMailMessage message = new SimpleMailMessage();

    String mailBody = "Welcome " + email.getUsername() + "!\n\n" +
            email.getKingdomName() + " is ready! You just need to confirm your email address\n" +
            "and then you are ready to conquer the world :)\n" +
            " Please confirm your email address by opening the following url: \n" +
            email.getContext().get("verificationURL") + "\n\n" +
            "Confirm Email Address\n\n" +
            " — The Tribes Team\n";



    message.setTo(email.getRecipientEmail());
    message.setFrom(email.getSenderEmail());
    message.setReplyTo(email.getSenderEmail());
    message.setSentDate(Calendar.getInstance().getTime());
    message.setSubject(email.getSubject());
    message.setText(mailBody);
    mailSender.send(message);
  }

  @Override
  public void sendHtmlMail(AbstractEmailContext email) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(email.getContext());

    String emailContent = templateEngine.process(email.getTemplateLocation(), context);

    mimeMessageHelper.setTo(email.getRecipientEmail());
    mimeMessageHelper.setSubject(email.getSubject());
    mimeMessageHelper.setFrom(email.getFrom());
    mimeMessageHelper.setText(emailContent, true);
    mailSender.send(message);
  }
}