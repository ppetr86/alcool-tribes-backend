package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmail;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service("emailService")
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Override
  public void sendMailWithHtmlAndPlainText(AbstractEmail email) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message,
            MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
            StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(email.getContext());

    String htmlMail = templateEngine.process(email.getTemplateLocation(), context);
    String textMail = "Welcome " + email.getUsername() + "!\n\n" +
            email.getKingdomName() + " is ready! You just need to confirm your email address\n" +
            "and then you are ready to conquer the world :)\n" +
            " Please confirm your email address by opening the following url: \n" +
            email.getContext().get("verificationURL") + "\n\n" +
            "Confirm Email Address\n\n" +
            " — The Tribes Team\n";

    helper.setTo(email.getRecipientEmail());
    helper.setSubject(email.getSubject());
    helper.setFrom(email.getSenderEmail());
    helper.setText(textMail,htmlMail);
    mailSender.send(message);
  }
}