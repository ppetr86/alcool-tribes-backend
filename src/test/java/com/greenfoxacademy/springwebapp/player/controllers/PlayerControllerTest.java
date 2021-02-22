package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.configuration.email.EmailService;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class PlayerControllerTest {

  private PlayerController playerController;
  private PlayerService registrationService;
  private EmailService emailService;

  @Before
  public void setup() {
    registrationService = Mockito.mock(PlayerService.class);
    emailService = Mockito.mock(EmailService.class);
    playerController = new PlayerController(registrationService,emailService);
  }

  @Test
  public void registerUserShouldSaveUserAndReturn201() {

    PlayerResponseDTO
        playerResponseDTO = new PlayerResponseDTO(1, "user1", "email@email.com", 1, "avatar", 1);
    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("user1", "user1234", "email@email.com");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    Mockito.when(registrationService.saveNewPlayer(playerRegistrationRequestDTO)).thenReturn(playerResponseDTO);

    ResponseEntity<?> response = playerController.registerUser(playerRegistrationRequestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
    Assert.assertEquals("email@email.com", ((PlayerResponseDTO) response.getBody()).getEmail());
    Assert.assertEquals("avatar", ((PlayerResponseDTO) response.getBody()).getAvatar());
    Assert.assertEquals("user1", ((PlayerResponseDTO) response.getBody()).getUsername());
    Assert.assertEquals(1, ((PlayerResponseDTO) response.getBody()).getKingdomId());
    Assert.assertEquals(1, ((PlayerResponseDTO) response.getBody()).getId());
  }

  @Test
  public void registerUserShouldSaveUserAndReturnCorrectKingdomId() {

    PlayerResponseDTO
        playerResponseDTO = new PlayerResponseDTO(1, "user1", "email@rmail.com", 1, "avatar", 1);
    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("user1", "user1234", "email");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    Mockito.when(registrationService.saveNewPlayer(playerRegistrationRequestDTO)).thenReturn(playerResponseDTO);

    ResponseEntity<PlayerResponseDTO> response =
        (ResponseEntity<PlayerResponseDTO>) playerController.registerUser(playerRegistrationRequestDTO, bindingResult);

    Assert.assertEquals(1, response.getBody().getKingdomId());
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());

  }

}