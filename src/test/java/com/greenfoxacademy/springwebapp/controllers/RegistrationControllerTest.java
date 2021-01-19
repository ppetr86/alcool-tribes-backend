package com.greenfoxacademy.springwebapp.controllers;

import static org.junit.Assert.*;

import com.greenfoxacademy.springwebapp.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
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
  public void registerUserShouldSaveUser() {
    //Arrange
    UserDTO userDTO = new UserDTO("user1", "password", "email@rmail.com", "my kingdom");
    UserEntity userEntity = new UserEntity("user1", "password", "email@rmail.com");
    KingdomEntity kingdomEntity = new KingdomEntity(1, userDTO.getKingdomname());
    //Act
    Mockito.when(registrationService.saveUser(userDTO)).thenReturn(userEntity);
    Mockito.when(userEntity.getKingdomEntity().getId()).thenReturn(kingdomEntity.getId());
    ResponseEntity<?> response = registrationController.registerUser(userDTO);
    //Assert
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }
}