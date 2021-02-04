package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlayerServiceTest {

  private PlayerService playerService;
  private PlayerRepository playerRepository;
  private PasswordEncoder passwordEncoder;
  private BuildingService buildingService;

  @Before
  public void setUp(){
    playerRepository = Mockito.mock(PlayerRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    buildingService = Mockito.mock(BuildingService.class);
    playerService = new PlayerServiceImpl(playerRepository, passwordEncoder, buildingService);
  }

  @Test
  public void findCorrectPlayerWithFindByUsername(){
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    Mockito
      .when(playerRepository.findByUsername("Mark"))
      .thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerService.findByUsername("Mark");

    Assert.assertEquals("Mark", fakePlayer.getUsername());
    Assert.assertEquals("markmark", fakePlayer.getPassword());
  }

  @Test
  public void returnNullWithFindByUsernameIfTheGivenNameIsIncorrect(){
    PlayerEntity playerEntity = new PlayerEntity("Mark", "markmark");

    Mockito
      .when(playerRepository.findByUsername("Mark"))
      .thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerService.findByUsername("BadMark");

    Assert.assertNull(fakePlayer);
  }


  @Test
  public void findByUserAndPasswordShouldReturnCorrectPlayer(){
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
  public void findByUserAndPasswordShouldReturnNullIfGivenPasswordIsIncorrect(){
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
  public void findByUserAndPasswordShouldReturnNullIfGivenUsernameIsIncorrect(){
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
}