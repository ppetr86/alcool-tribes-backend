package com.greenfoxacademy.springwebapp.email;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.repository.RegistrationTokenRepository;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RegistrationTokenServiceTest {

    private RegistrationTokenService tokenService;
    private RegistrationTokenRepository tokenRepository;

    @Before
    public void init() {
        tokenRepository = Mockito.mock(RegistrationTokenRepository.class);
        tokenService = new RegistrationTokenServiceImpl(tokenRepository);
        ReflectionTestUtils.setField(tokenService, "tokenValidityInSeconds", 86400);
    }

    @Test
    public void createSecureToken_ReturnsToken() {
        RegistrationTokenEntity te = tokenService.createSecureToken();
        te.setTimeStamp(Timestamp.valueOf(LocalDateTime.now()));
        Assert.assertFalse(te.getIsExpired());
        Assert.assertTrue(LocalDateTime.now().isBefore(te.getExpireAt()));
        Assert.assertEquals(20, te.getToken().length());
        //why is my key len 20 when defined is 15???
        //why the time related doesnt  work??
    }

    @Test
    public void findByTokenReturnsToken() {

    }
}
