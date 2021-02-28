package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.List;

public class PlayerControllerTest {

  private PlayerController playerController;
  private PlayerService registrationService;
  private BuildingService buildingService;
  private ResourceService resourceService;
  private PlayerRepository playerRepository;


  @Before
  public void setup() {
    registrationService = Mockito.mock(PlayerService.class);
    playerController = new PlayerController(registrationService);
    buildingService = Mockito.mock(BuildingService.class);
    resourceService = Mockito.mock(ResourceService.class);
    playerRepository = Mockito.mock(PlayerRepository.class);
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

  @Test
  public void registerPlayerShouldSaveNewPlayerAndReturnCorrectStatusCode() {

    KingdomEntity kingdomEntity = KingdomFactory.createKingdomEntityWithId(1L);
    PlayerEntity fakePlayerEntity = PlayerFactory.createPlayer(1L, kingdomEntity);
    PlayerRegistrationRequestDTO playerRegistrationRequestDTO =
        new PlayerRegistrationRequestDTO("user1", "user1234", "email");
    List<BuildingEntity> fakeListBuilding = BuildingFactory.createBuildings(kingdomEntity);

    List<ResourceEntity> fakeListResources = ResourceFactory.createResourcesWithAllData(kingdomEntity);

    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    Mockito.when(buildingService.createDefaultBuildings(kingdomEntity)).thenReturn(fakeListBuilding);
    Mockito.when(resourceService.createDefaultResources(kingdomEntity)).thenReturn(fakeListResources);
    Mockito.when(playerRepository.save(fakePlayerEntity)).thenReturn(fakePlayerEntity);

    ResponseEntity<PlayerResponseDTO> response =
        (ResponseEntity<PlayerResponseDTO>) playerController.registerUser(playerRegistrationRequestDTO, bindingResult);

    Assert.assertEquals(HttpStatus.valueOf(201), response.getStatusCode());
  }

}