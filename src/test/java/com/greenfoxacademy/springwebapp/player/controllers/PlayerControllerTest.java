package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.DeletedPlayerDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.enums.RoleType;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        .roleType(RoleType.ROLE_USER)
        .build();

    PlayerRegisterRequestDTO playerRegistrationRequestDTO =
        new PlayerRegisterRequestDTO("user1", "user1234", "email@email.com");
    Mockito.when(playerService.saveNewPlayer(playerRegistrationRequestDTO)).thenReturn(playerEntity);
    ResponseEntity<?> response = playerController.registerUser(playerRegistrationRequestDTO);
    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

  @Test(expected = RuntimeException.class)
  public void registerUser_takenUsername() {
    PlayerRegisterRequestDTO rqst =
        new PlayerRegisterRequestDTO("user1", "user1234", "email");

    Mockito.when(playerService.registerNewPlayer(rqst)).thenThrow(RuntimeException.class);

    ResponseEntity<?> response = playerController.registerUser(rqst);
    Assert.assertEquals("Username is already taken.", ((ErrorDTO) response.getBody()).getMessage());
    Assert.assertEquals(HttpStatus.valueOf(409), response.getStatusCode());
  }

  @Test
  public void deletePlayer_Should_DeleteAPlayer() {
    List<PlayerEntity> players = Arrays.asList(
        PlayerFactory.createPlayer(1L, null, true, "firstName"),
        PlayerFactory.createPlayer(2L, null, true, "secondName")
    );
    DeletedPlayerDTO deletedDTO = new DeletedPlayerDTO(true, players.get(1).getUsername());

    Mockito.when(playerService.deletePlayer(2L)).thenReturn(deletedDTO);

    ResponseEntity<DeletedPlayerDTO> response = playerController.deletePlayer(2L);

    Assert.assertTrue(Objects.requireNonNull(response.getBody()).isDeleted());
    Assert.assertEquals("secondName player deleted.", response.getBody().getDeletedPlayerName());
  }

  @Test(expected = IdNotFoundException.class)
  public void deletePlayer_Should_Throw_IdNotFoundException() {
    Mockito.when(playerService.deletePlayer(2L)).thenThrow(IdNotFoundException.class);

    ResponseEntity<DeletedPlayerDTO> response = playerController.deletePlayer(2L);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}