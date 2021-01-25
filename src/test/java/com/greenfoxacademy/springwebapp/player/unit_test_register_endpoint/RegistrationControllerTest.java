package com.greenfoxacademy.springwebapp.player.unit_test_register_endpoint;

import com.greenfoxacademy.springwebapp.player.controllers.PlayerController;
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

public class RegistrationControllerTest {

  private PlayerController playerController;
  private PlayerService registrationService;

  @Before
  public void setup() {
    registrationService = Mockito.mock(PlayerService.class);
    playerController = new PlayerController(registrationService);
  }

  @Test
  public void registerUserShouldSaveUserAndReturn201() {
    //Arrange
    PlayerResponseDTO
        playerResponseDTO = new PlayerResponseDTO(1, "user1", "email@rmail.com", 1, "avatar", 1);
    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("user1", "user1234", "email");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerResponseDTO);
    ResponseEntity<?> response = playerController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

  @Test
  public void registerUserShouldSaveUserAndReturnCorrectKingdomId() {
    //Arrange
    PlayerResponseDTO
        playerResponseDTO = new PlayerResponseDTO(1, "user1", "email@rmail.com", 1, "avatar", 1);
    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("user1", "user1234", "email");

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerResponseDTO);
    ResponseEntity<PlayerResponseDTO> response =
        (ResponseEntity<PlayerResponseDTO>) playerController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals(1, response.getBody().getKingdomId());
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());

  }

}