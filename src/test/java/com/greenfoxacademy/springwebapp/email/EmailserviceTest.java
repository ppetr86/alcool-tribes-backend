package com.greenfoxacademy.springwebapp.email;

import com.greenfoxacademy.springwebapp.email.context.AccountVerificationEmail;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.repository.RegistrationTokenRepository;
import com.greenfoxacademy.springwebapp.email.services.EmailServiceImpl;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenServiceImpl;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailserviceTest {

    @Mock
    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;
    private EmailServiceImpl emailService;
    private RegistrationTokenService tokenService;
    @Mock
    private RegistrationTokenRepository registrationTokenRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tokenService = new RegistrationTokenServiceImpl(registrationTokenRepository);
        templateEngine = new SpringTemplateEngine();
        emailService = new EmailServiceImpl(mailSender, templateEngine);
    }

    @Test
    public void sendMail_Test() throws MessagingException {
        KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
        PlayerEntity pl = PlayerFactory.createPlayer(1L,null);
        RegistrationTokenEntity token = tokenService.createSecureToken(pl);
        token.setPlayer(kingdom.getPlayer());
        AccountVerificationEmail email = new AccountVerificationEmail();

        email.init(kingdom.getPlayer());
        email.setToken(token.getToken());
        email.buildVerificationUrl("http://localhost:8080", token.getToken());
        //doNothing().when(mailSender).send(mailSender.createMimeMessage());
        //verify(javaMailSender, times(1)).send(emailCaptor.capture());

        emailService.sendMailWithHtmlAndPlainText(email);

        Assert.assertEquals("testUsername", email.getUsername());
        Assert.assertEquals("test@mail.com", email.getRecipientEmail());
        Assert.assertEquals("testKingdom", email.getKingdomName());
    }
}