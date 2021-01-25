package com.greenfoxacademy.springwebapp.registerTests.unit_test_register_endpoint;

import com.greenfoxacademy.springwebapp.register.controllers.RegistrationController;
import com.greenfoxacademy.springwebapp.register.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.register.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.register.models.dtos.ErrorDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.PlayerDTO;
import com.greenfoxacademy.springwebapp.register.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.register.services.RegistrationService;
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
    PlayerDTO playerDTO = new PlayerDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    //Act
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    ResponseEntity<?> response = registrationController.registerUser(playerDTO, bindingResult);
    //Assert
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

  @Test
  public void registerUserShouldSaveUserAndReturnCorrectKingdomId() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    ResponseEntity<RegisterResponseDTO> response =
        (ResponseEntity<RegisterResponseDTO>) registrationController.registerUser(playerDTO, bindingResult);
    //Assert
    Assert.assertEquals(1, response.getBody().getKingdomId());
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());

  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAnd400() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO(null, "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(PlayerDTO.class, playerDTO.getUsername());

    //Act
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    ResponseEntity<?> response = registrationController.registerUser(playerDTO, bindingResult);
    //Assert
    Assert.assertEquals("Username is required.", ((ErrorDTO) response.getBody()).getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfPasswordAnd400() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO("user1", null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerDTO, bindingResult);
    //Assert
    Assert.assertEquals("Password is required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAndPasswordAnd400() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO(null, null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerDTO, bindingResult);
    //Assert
    Assert.assertEquals("Username and password are required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAlreadyTakenAnd409() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO(1,"user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    Mockito.when(registrationService.findByUsername(playerDTO.getUsername())).thenReturn(playerEntity);
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerDTO, bindingResult);
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    //Assert
    Assert.assertEquals("Username is already taken.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfShortPasswordAnd406() {
    //Arrange
    PlayerDTO playerDTO = new PlayerDTO(1,"user1", "pasword", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, playerDTO.getKingdomname());
    PlayerEntity playerEntity = new PlayerEntity(playerDTO.getUsername(), playerDTO.getPassword(),
        playerDTO.getEmail(), kingdomEntity);
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    //Act
    ResponseEntity<ErrorDTO> response =
        (ResponseEntity<ErrorDTO>) registrationController.registerUser(playerDTO, bindingResult);
    Mockito.when(registrationService.savePlayer(playerDTO)).thenReturn(playerEntity);
    //Assert
    Assert.assertEquals("Password must be 8 characters.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(406), response.getStatusCode());

    //binding result issue, need to somehow give binding result existing user so it properly gives the error
  }


}