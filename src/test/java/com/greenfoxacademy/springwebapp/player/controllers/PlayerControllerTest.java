package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class PlayerControllerTest {

    private PlayerController playerController;
    private PlayerService playerService;

    @Before
    public void setup() {
        playerService = Mockito.mock(PlayerService.class);
        playerController = new PlayerController(playerService);
    }

    @Test
    public void registerUserShouldSaveUserAndReturn201() {

        KingdomEntity ke = KingdomFactory.createKingdomEntityWithId(1L);
        PlayerEntity playerEntity = PlayerEntity.builder()
                .email("email@email.com")
                .avatar("avatar")
                .username("user1")
                .kingdom(ke)
                .id(1L)
                .password("password")
                .isAccountVerified(true)
                .build();

        PlayerRegisterRequestDTO playerRegistrationRequestDTO =
                new PlayerRegisterRequestDTO("user1", "user1234", "email@email.com");
        Mockito.when(playerService.saveNewPlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
        ResponseEntity<?> response = playerController.registerUser(playerRegistrationRequestDTO);
        Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
    }

    @Test(expected = UsernameIsTakenException.class)
    public void registerUser_takenUsername() {
        PlayerRegisterRequestDTO rqst =
                new PlayerRegisterRequestDTO("user1", "user1234", "email");

        Mockito.when(playerService.registerNewPlayer(rqst)).thenThrow(UsernameIsTakenException.class);

        ResponseEntity<?> response = playerController.registerUser(rqst);
        Assert.assertEquals("Username is already taken.", ((ErrorDTO) response.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
    }


    /*@Test(expected = MethodArgumentNotValidException.class)
    public void registerUser_blankPwd() {
        PlayerRegisterRequestDTO rqst =
                new PlayerRegisterRequestDTO("user1", null, "email");
        KingdomEntity ke = KingdomFactory.createKingdomEntityWithId(1L);
        PlayerEntity playerEntity = PlayerEntity.builder()
                .email("email@email.com")
                .avatar("avatar")
                .username("user1")
                .kingdom(ke)
                .id(1L)
                .password("password")
                .isAccountVerified(true)
                .build();
        Mockito.when(playerService.registerNewPlayer(rqst)).thenReturn(playerEntity);
        ResponseEntity<?> response = playerController.registerUser(rqst);
        Assert.assertEquals("Password is required.", ((ErrorDTO) response.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test(expected = MethodArgumentNotValidException.class)
    public void registerUser_blankUsername() {
        PlayerRegisterRequestDTO rqst =
                new PlayerRegisterRequestDTO(null, "password", "email");

        ResponseEntity<?> response = playerController.registerUser(rqst);
        Assert.assertEquals("Username is required.", ((ErrorDTO) response.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test(expected = MethodArgumentNotValidException.class)
    public void registerUser_pwdLessThan8Chars() {
        PlayerRegisterRequestDTO rqst =
                new PlayerRegisterRequestDTO("username", "pwd", "email");

        ResponseEntity<?> response = playerController.registerUser(rqst);
        Assert.assertEquals("Password must be 8 characters.", ((ErrorDTO) response.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test(expected = MethodArgumentNotValidException.class)
    public void registerUser_EmailWrongFormat() {
        PlayerRegisterRequestDTO rqst =
                new PlayerRegisterRequestDTO("username", "pwd", "email@hello");

        ResponseEntity<?> response = playerController.registerUser(rqst);
        Assert.assertEquals("Kindly provide valid e-mail address.", ((ErrorDTO) response.getBody()).getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }*/
}