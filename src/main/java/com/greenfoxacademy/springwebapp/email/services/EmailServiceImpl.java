package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Override
  public Boolean sendMailWithHtmlAndPlainText(AbstractEmail email)
      throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(email.getContext());

    String htmlMail = templateEngine.process(
        email.getTemplateLocationHtml(), context);
    String textMail = templateEngine.process(
        email.getTemplateLocationText(), context);

    helper.setTo(email.getRecipientEmail());
    helper.setSubject(email.getSubject());
    helper.setFrom(email.getSenderEmail());
    helper.setText(textMail, htmlMail);
    mailSender.send(message);
    log.info("Success: email sent");
    return true;
  }
}