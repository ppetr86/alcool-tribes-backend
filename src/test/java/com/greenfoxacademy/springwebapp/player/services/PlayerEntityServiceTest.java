package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerEntityRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

public class PlayerEntityServiceTest {

  private PlayerEntityService playerEntityService;
  private PlayerEntityRepository playerEntityRepository;
  private PasswordEncoder passwordEncoder;

  @Before
  public void init(){
    playerEntityRepository = Mockito.mock(PlayerEntityRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    playerEntityService = new PlayerEntityServiceImp(playerEntityRepository, passwordEncoder);
  }

  @Test
  public void countPlayersMethodShouldReturnCorrectNumber(){
    List<PlayerEntity> fakeList = Arrays.asList(
      new PlayerEntity("Mark"),
      new PlayerEntity("Zdenek"),
      new PlayerEntity("Ahmed"),
      new PlayerEntity("Petr")
    );

    Mockito.when(playerEntityRepository.count()).thenReturn((long)fakeList.size());

    Assert.assertEquals(4, playerEntityService.countPlayers());
  }

  @Test
  public void findCorrectPlayerWithFindByUsername(){
    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");

    Mockito.when(playerEntityRepository.findByUsername("Mark")).thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerEntityService.findByUsername("Mark");

    Assert.assertEquals("Mark", fakePlayer.getUsername());
    Assert.assertEquals("mark", fakePlayer.getPassword());
  }

  @Test
  public void findByUserAndPasswordShouldReturnCorrectPlayer(){
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petr");

    Mockito.when(playerEntityService.findByUsername("Petr")).thenReturn(playerEntity);
    Mockito.when(passwordEncoder.matches("petr", playerEntity.getPassword())).thenReturn(true);

    PlayerEntity mockPlayer = playerEntityService.findByUsernameAndPassword("Petr", "petr");

    Assert.assertEquals("Petr", mockPlayer.getUsername());
    Assert.assertEquals("petr", mockPlayer.getPassword());
  }
}