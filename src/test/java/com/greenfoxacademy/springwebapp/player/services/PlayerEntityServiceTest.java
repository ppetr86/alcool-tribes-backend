package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerEntityRepository;
import com.greenfoxacademy.springwebapp.player.repositories.RoleEntityRepository;
import javafx.beans.binding.When;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerEntityServiceTest {

  private PlayerEntityService playerEntityService;
  private PlayerEntityRepository playerEntityRepository = Mockito.mock(PlayerEntityRepository.class);


  @Before
  public void init(){
    RoleEntityRepository roleEntityRepository = Mockito.mock(RoleEntityRepository.class);
    PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    playerEntityService = new PlayerEntityServiceImp(playerEntityRepository, roleEntityRepository, passwordEncoder);
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
    PlayerEntity fakePlayer = new PlayerEntity("Mark");
    Mockito.when(playerEntityRepository.findByUsername("Mark")).thenReturn(fakePlayer);

    PlayerEntity player = playerEntityService.findByUsername("Mark");

    Assert.assertEquals("Mark", player.getUsername());
  }
}