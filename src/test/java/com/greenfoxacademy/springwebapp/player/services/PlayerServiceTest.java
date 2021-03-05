package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

public class PlayerServiceTest {

  private PlayerService playerService;
  private PlayerRepository playerRepository;
  private PasswordEncoder passwordEncoder;
  private BuildingService buildingService;
  private ResourceService resourceService;

  @Before
  public void setUp() {
    playerRepository = Mockito.mock(PlayerRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    buildingService = Mockito.mock(BuildingService.class);
    playerService = new PlayerServiceImpl(playerRepository, passwordEncoder, buildingService, resourceService);
  }

  @Test
  public void findCorrectPlayerWithFindByUsername() {
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    Mockito
        .when(playerRepository.findByUsername("Mark"))
        .thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerService.findByUsername("Mark");

    Assert.assertEquals("Mark", fakePlayer.getUsername());
    Assert.assertEquals("markmark", fakePlayer.getPassword());
  }

  @Test
  public void returnNullWithFindByUsernameIfTheGivenNameIsIncorrect() {
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    Mockito
        .when(playerRepository.findByUsername("Mark"))
        .thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerService.findByUsername("BadMark");

    Assert.assertNull(fakePlayer);
  }


  @Test
  public void findByUserAndPasswordShouldReturnCorrectPlayer() {
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petrpetr");

    Mockito
        .when(playerService.findByUsername("Petr"))
        .thenReturn(playerEntity);

    Mockito
        .when(passwordEncoder.matches("petrpetr", playerEntity.getPassword()))
        .thenReturn(true);

    PlayerEntity mockPlayer = playerService.findByUsernameAndPassword("Petr", "petrpetr");

    Assert.assertEquals("Petr", mockPlayer.getUsername());
    Assert.assertEquals("petrpetr", mockPlayer.getPassword());

    Assert.assertNotEquals("Petr", "Petrr");
    Assert.assertNotEquals("petrpetr", "password");
  }

  @Test
  public void findByUserAndPasswordShouldReturnNullIfGivenPasswordIsIncorrect() {
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petrpetr");

    Mockito
        .when(playerService.findByUsername("Petr"))
        .thenReturn(playerEntity);

    Mockito
        .when(passwordEncoder.matches("petrpetr", playerEntity.getPassword()))
        .thenReturn(true);

    PlayerEntity mockPlayer = playerService.findByUsernameAndPassword("Petr", "badPassword");

    Assert.assertNull(mockPlayer);
  }

  @Test
  public void findByUserAndPasswordShouldReturnNullIfGivenUsernameIsIncorrect() {
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petrpetr");

    Mockito
        .when(playerService.findByUsername("Petr"))
        .thenReturn(playerEntity);

    Mockito
        .when(passwordEncoder.matches("petrpetr", playerEntity.getPassword()))
        .thenReturn(true);

    PlayerEntity mockPlayer = playerService.findByUsernameAndPassword("NoPetr", "petrpetr");

    Assert.assertNull(mockPlayer);
  }


  @Test
  public void findPlayersAroundMeShouldReturnAllPlayers() {
    KingdomEntity kingdom1 = KingdomFactory.createFullKingdom(1L, 1L);
    PlayerEntity player1 = PlayerFactory.createPlayer(1L, kingdom1);
    player1.setAvatar("avatar1");
    player1.setPoints(10);
    KingdomEntity kingdom2 = KingdomFactory.createFullKingdom(2L, 2L);
    PlayerEntity player2 = PlayerFactory.createPlayer(2L, kingdom2);
    player2.setPoints(20);
    player2.setAvatar("avatar2");
    KingdomEntity kingdom3 = KingdomFactory.createFullKingdom(3L, 3L);
    PlayerEntity player3 = PlayerFactory.createPlayer(3L, kingdom3);
    player3.setPoints(30);
    player3.setAvatar("avatar3");
    List<PlayerEntity> fakeListOfAllPlayers = Arrays.asList(player1, player2, player3);

    Mockito.when(playerRepository.findAll()).thenReturn(fakeListOfAllPlayers);
    PlayerListResponseDTO response = playerService.findPlayersAroundMe(kingdom3, null);
    
    Assert.assertEquals(2, response.getPlayers().size());
  }
}