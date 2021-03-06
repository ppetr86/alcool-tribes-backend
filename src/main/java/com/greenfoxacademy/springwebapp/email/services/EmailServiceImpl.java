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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("emailService")
@AllArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Override
  public Boolean sendMailWithHtmlAndPlainText(AbstractEmail email) throws MessagingException, IOException {
    MimeMessage message = mailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(message,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());
    Context context = new Context();
    context.setVariables(email.getContext());

    String htmlMail = templateEngine.process(email.getTemplateLocation(), context);
    String textMail = verificationMail(email);

    helper.setTo(email.getRecipientEmail());
    helper.setSubject(email.getSubject());
    helper.setFrom(email.getSenderEmail());
    helper.setText(textMail, htmlMail);
    mailSender.send(message);
    log.info("Success: email sent");
    return true;
  }

  private String verificationMail(AbstractEmail email) {
    return "Welcome " + email.getUsername() + "!\n\n"
        + email.getKingdomName() + " is ready! You just need to confirm your email address\n"
        + "and then you are ready to conquer the world :)\n"
        + " Please confirm your email address by opening the following url: \n"
        + email.getContext().get("verificationURL") + "\n\n"
        + "Confirm Email Address\n\n"
        + " — The Tribes Team\n";
  }

  private String readVerificationMailFromFile(AbstractEmail email) throws IOException {
    Path path = Paths.get("/resources/emails/registrationEmail.txt");
    Stream<String> lines = Files.lines(path);
    String emailMessage = lines.collect(Collectors.joining("\n"));
    lines.close();
    //String emailMessage = Files.lines(path, StandardCharsets.UTF_8).collect(Collectors.joining("\n"));
    //String emailMessage = String.join("\n", Files.readAllLines(path));

    emailMessage = emailMessage.replaceAll("USERNAME", email.getUsername());
    emailMessage = emailMessage.replaceAll("KINGDOMNAME", email.getKingdomName());
    emailMessage = emailMessage.replaceAll("VERIFICATIONURL", (String) email.getContext().get("verificationURL"));
    return emailMessage;
  }
}