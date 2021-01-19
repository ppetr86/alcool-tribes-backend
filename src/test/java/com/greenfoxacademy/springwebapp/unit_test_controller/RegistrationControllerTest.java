package com.greenfoxacademy.springwebapp.unit_test_controller;

import com.greenfoxacademy.springwebapp.controllers.RegistrationController;
import com.greenfoxacademy.springwebapp.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserErrorDTO;
import com.greenfoxacademy.springwebapp.services.RegistrationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    UserDTO userDTO = new UserDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    ResponseEntity<?> response = registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

  @Test
  public void registerUserShouldSaveUserAndReturnCorrectKingdomId() {
    //Arrange
    UserDTO userDTO = new UserDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    ResponseEntity<RegisterResponseDTO> response =
        (ResponseEntity<RegisterResponseDTO>) registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals(1, response.getBody().getKingdomId());
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());

  }

  @Test
  public void registerUserShouldThrowErrorOfUsername() {
    //Arrange
    UserDTO userDTO = new UserDTO(null, "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    ResponseEntity<UserErrorDTO> response =
        (ResponseEntity<UserErrorDTO>) registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals("Username is required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfPassword() {
    //Arrange
    UserDTO userDTO = new UserDTO("user1", null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    ResponseEntity<UserErrorDTO> response =
        (ResponseEntity<UserErrorDTO>) registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals("Password is required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAndPassword() {
    //Arrange
    UserDTO userDTO = new UserDTO(null, null, "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    ResponseEntity<UserErrorDTO> response =
        (ResponseEntity<UserErrorDTO>) registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals("Username and password are required.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(400), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfUsernameAlreadyTaken() {
    //Arrange
    UserDTO userDTO = new UserDTO("user1", "password", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    Mockito.when(registrationService.findByUsername(userDTO.getUsername())).thenReturn(userEntity);
    ResponseEntity<UserErrorDTO> response =
        (ResponseEntity<UserErrorDTO>) registrationController.registerUser(userDTO);
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    //Assert
    Assert.assertEquals("Username is already taken.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
  }

  @Test
  public void registerUserShouldThrowErrorOfShortPassword() {
    //Arrange
    UserDTO userDTO = new UserDTO("user1", "pasword", "email@rmail.com", "my kingdom");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    UserEntity userEntity = new UserEntity(userDTO.getUsername(), userDTO.getPassword(),
        userDTO.getEmail(), kingdomEntity);
    //Act
    ResponseEntity<UserErrorDTO> response =
        (ResponseEntity<UserErrorDTO>) registrationController.registerUser(userDTO);
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    //Assert
    Assert.assertEquals("Password must be 8 characters.", response.getBody().getMessage());
    Assert.assertEquals(HttpStatus.valueOf(406), response.getStatusCode());
  }


}