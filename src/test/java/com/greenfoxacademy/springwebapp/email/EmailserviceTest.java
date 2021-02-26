package com.greenfoxacademy.springwebapp.email;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmailContext;
import com.greenfoxacademy.springwebapp.email.context.AccountVerificationEmailContext;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailServiceImpl;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;

public class EmailserviceTest {

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;
    private EmailServiceImpl emailService;
    private RegistrationTokenService tokenService;

    @Value("${site.base.url.https}")
    private String baseURL;

    @Before
    public void init(){
        mailSender = Mockito.mock(JavaMailSender.class);
        templateEngine = Mockito.mock(SpringTemplateEngine.class);
        emailService = new EmailServiceImpl(mailSender,templateEngine);
    }

    @Test
    public void sendMail_Test() throws MessagingException {
        KingdomEntity k = KingdomFactory.createFullKingdom(1L,1L);

        RegistrationTokenEntity token = tokenService.createSecureToken();
        token.setPlayer(k.getPlayer());
        AccountVerificationEmailContext email = new AccountVerificationEmailContext();

        email.init(k.getPlayer());
        email.setToken(token.getToken());
        email.buildVerificationUrl(baseURL, token.getToken());

        emailService.sendMailWithHtmlAndPlainText(email);

        Assert.assertEquals("testUsername", email.getUsername());
        Assert.assertEquals("test@mail.com", email.getRecipientEmail());
        Assert.assertEquals("testKingdom", email.getKingdomName());
    }
}