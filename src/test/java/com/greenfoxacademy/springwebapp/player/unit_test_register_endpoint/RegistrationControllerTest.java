package com.greenfoxacademy.springwebapp.player.unit_test_register_endpoint;

import com.greenfoxacademy.springwebapp.player.register.controllers.RegistrationController;
import com.greenfoxacademy.springwebapp.player.register.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.register.models.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.player.register.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.register.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.player.register.services.RegistrationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class RegistrationControllerTest {

  private RegistrationController registrationController;
  private RegistrationService registrationService;

  @Before
  public void setup(){
    registrationService = Mockito.mock(RegistrationService.class);
    registrationController = new RegistrationController(registrationService);
  }

  @Test
  public void registerUserShouldSaveUserAndReturn201() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<?> response = registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

  @Test
  public void registerUserShouldSaveUserAndReturnCorrectKingdomId() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<RegisterResponseDTO> response =
        (ResponseEntity<RegisterResponseDTO>) registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals(1, response.getBody().getKingdomId());
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());

  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAnd400() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO(null, "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(PlayerRegistrationRequestDTO.class, playerRegistrationRequestDTO
        .getUsername());

    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<?> response = registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals("Username is required.", ((ErrorDTO) response.getBody()).getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfPasswordAnd400() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO("user1", null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals("Password is required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAndPasswordAnd400() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO(null, null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    //Assert
    Assert.assertEquals("Username and password are required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAlreadyTakenAnd409() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO(1,"user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.findByUsername(playerRegistrationRequestDTO.getUsername())).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    //Assert
    Assert.assertEquals("Username is already taken.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfShortPasswordAnd406() {
    //Arrange
    PlayerRegistrationRequestDTO
        playerRegistrationRequestDTO = new PlayerRegistrationRequestDTO(1,"user1", "pasword", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerRegistrationRequestDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerRegistrationRequestDTO.getUsername(), playerRegistrationRequestDTO
        .getPassword(),
        playerRegistrationRequestDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerRegistrationRequestDTO, bindingResult);
    Mockito.when(registrationService.savePlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    //Assert
    Assert.assertEquals("Password must be 8 characters.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(406), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }


}