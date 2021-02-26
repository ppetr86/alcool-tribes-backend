package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.player.services.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class LoginControllerUnitTest {

    private LoginController loginController;
    private PlayerService playerService;
    private TokenService tokenService;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Before
    public void setUp() {
        tokenService = Mockito.mock(TokenService.class);
        playerService = Mockito.mock(PlayerService.class);
        loginController = new LoginController(playerService);
    }

    @Test
    public void postLoginShouldReturn200AndOkStatus() throws Exception, NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
        PlayerEntity entity = new PlayerEntity("Mark", "markmark");
        PlayerTokenDTO tokenDTO = new PlayerTokenDTO("12345");
        PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "markmark");
        Mockito
                .when(tokenService.generateTokenToLoggedInPlayer(entity))
                .thenReturn(tokenDTO);
        Mockito
                .when(playerService.findByUsernameAndPassword(requestDTO.getUsername(), requestDTO.getPassword()))
                .thenReturn(entity);
        ResponseEntity<PlayerTokenDTO> response = loginController.login(requestDTO);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /*@Test
    public void postLoginShould400ErrorStatusAndUsernameIsRequiredMessage() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
        PlayerRequestDTO requestDTO = new PlayerRequestDTO(null, "markmark");
        ResponseEntity<PlayerTokenDTO> response = loginController.login(requestDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }*/

    /*@Test
    public void postLoginShould400ErrorStatusAndPasswordIsRequiredMessage() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO("Mark", null);
        ResponseEntity<PlayerTokenDTO> response = loginController.login(playerRequestDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }*/

    /*@Test
    public void loginShould400_OnPwdLessThan8Chars() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
        PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "mark");
        ResponseEntity<PlayerTokenDTO> response = loginController.login(requestDTO);
        Errors errors = new BeanPropertyBindingResult(requestDTO, "password");
        validator.validate(requestDTO, errors);
        Assert.assertTrue(errors.hasErrors());

    }*/

    /*@Test(expected = MethodArgumentNotValidException.class)
    public void loginShould400_OnMissingUsernameAndPwd() throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {
        PlayerRequestDTO playerRequestDTO = new PlayerRequestDTO();
        ResponseEntity<?> response = loginController.login(playerRequestDTO);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals("Username and password are required.", ((ErrorDTO) response.getBody()).getMessage());
    }*/


    @Test(expected = IncorrectUsernameOrPwdException.class)
    public void loginShould401_WrongPasswordAndOrUsername() throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {
        PlayerRequestDTO requestDTO = new PlayerRequestDTO("Mark", "badPassword");
        Mockito
                .when(playerService.loginPlayer(requestDTO))
                .thenThrow(IncorrectUsernameOrPwdException.class);
        ResponseEntity<?> response = loginController.login(requestDTO);
        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertEquals("Username or password is incorrect.", ((ErrorDTO) response.getBody()).getMessage());
    }
}