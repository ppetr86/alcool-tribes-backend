package com.greenfoxacademy.springwebapp.email;

import com.greenfoxacademy.springwebapp.email.context.AccountVerificationEmail;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.repository.RegistrationTokenRepository;
import com.greenfoxacademy.springwebapp.email.services.EmailServiceImpl;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenServiceImpl;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;

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

        RegistrationTokenEntity token = tokenService.createSecureToken();
        token.setPlayer(kingdom.getPlayer());
        AccountVerificationEmail email = new AccountVerificationEmail();

        email.init(kingdom.getPlayer());
        email.setToken(token.getToken());
        email.buildVerificationUrl("http://localhost:8080", token.getToken());

        ArgumentCaptor<SimpleMailMessage> emailCaptor =
                ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(emailCaptor.capture());

        emailService.sendMailWithHtmlAndPlainText(email);

        Assert.assertEquals("testUsername", email.getUsername());
        Assert.assertEquals("test@mail.com", email.getRecipientEmail());
        Assert.assertEquals("testKingdom", email.getKingdomName());
    }
}