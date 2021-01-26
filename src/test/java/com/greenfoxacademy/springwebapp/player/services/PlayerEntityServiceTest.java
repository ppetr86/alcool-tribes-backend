package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerEntityRepository;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PlayerEntityServiceTest {

  private PlayerEntityService playerEntityService;
  private PlayerEntityRepository playerEntityRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider mockJwtProvider;

  @Before
  public void setUp(){
    playerEntityRepository = Mockito.mock(PlayerEntityRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    mockJwtProvider = Mockito.mock(JwtProvider.class);
    playerEntityService = new PlayerEntityServiceImp(playerEntityRepository, passwordEncoder, mockJwtProvider);
  }

  //Tests for findByUsername method
  @Test
  public void findCorrectPlayerWithFindByUsername(){
    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");

    Mockito.when(playerEntityRepository.findByUsername("Mark")).thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerEntityService.findByUsername("Mark");

    Assert.assertEquals("Mark", fakePlayer.getUsername());
    Assert.assertEquals("mark", fakePlayer.getPassword());
  }

  @Test
  public void returnNullWithFindByUsernameIfTheGivenNameIsIncorrect(){
    PlayerEntity playerEntity = new PlayerEntity("Mark", "mark");

    Mockito.when(playerEntityRepository.findByUsername("Mark")).thenReturn(playerEntity);

    PlayerEntity fakePlayer = playerEntityService.findByUsername("BadMark");

    Assert.assertNull(fakePlayer);
  }


  // Tests for findByUsernameAndPassword
  @Test
  public void findByUserAndPasswordShouldReturnCorrectPlayer(){
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petr");

    Mockito.when(playerEntityService.findByUsername("Petr")).thenReturn(playerEntity);
    Mockito.when(passwordEncoder.matches("petr", playerEntity.getPassword())).thenReturn(true);

    PlayerEntity mockPlayer = playerEntityService.findByUsernameAndPassword("Petr", "petr");

    Assert.assertEquals("Petr", mockPlayer.getUsername());
    Assert.assertEquals("petr", mockPlayer.getPassword());

    Assert.assertNotEquals("Petr", "Petrr");
    Assert.assertNotEquals("petr", "password");
  }

  @Test
  public void findByUserAndPasswordShouldReturnNullIfGivenPasswordIsIncorrect(){
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petr");

    Mockito.when(playerEntityService.findByUsername("Petr")).thenReturn(playerEntity);
    Mockito.when(passwordEncoder.matches("petr", playerEntity.getPassword())).thenReturn(true);

    PlayerEntity mockPlayer = playerEntityService.findByUsernameAndPassword("Petr", "badPassword");

    Assert.assertNull(mockPlayer);
  }

  @Test
  public void findByUserAndPasswordShouldReturnNullIfGivenUsernameIsIncorrect(){
    PlayerEntity playerEntity = new PlayerEntity("Petr", "petr");

    Mockito.when(playerEntityService.findByUsername("Petr")).thenReturn(playerEntity);
    Mockito.when(passwordEncoder.matches("petr", playerEntity.getPassword())).thenReturn(true);

    PlayerEntity mockPlayer = playerEntityService.findByUsernameAndPassword("NoPetr", "petr");

    Assert.assertNull(mockPlayer);
  }
}