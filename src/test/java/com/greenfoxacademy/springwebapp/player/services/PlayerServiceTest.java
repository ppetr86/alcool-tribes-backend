package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
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
  private EmailService emailService;
  private RegistrationTokenService registrationTokenService;
  private TokenService tokenService;

  @Before
  public void setUp() {
    playerRepository = Mockito.mock(PlayerRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    buildingService = Mockito.mock(BuildingService.class);
    emailService = Mockito.mock(EmailService.class);
    registrationTokenService = Mockito.mock(RegistrationTokenService.class);
    tokenService = Mockito.mock(TokenService.class);
    playerService = new PlayerServiceImpl(playerRepository, passwordEncoder, buildingService, emailService, registrationTokenService, tokenService);
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
  public void loginPlayerShouldReturnPlayerRequestDTO(){
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");
    PlayerEntity pl = PlayerFactory.createPlayer(1L,null);
    Mockito.when(playerService.findByUsernameAndPassword(rqst.getUsername(),rqst.getPassword()))
    .thenReturn(pl);
    PlayerTokenDTO tkn = new PlayerTokenDTO("MY_TOKEN");
    Mockito.when(tokenService.generateTokenToLoggedInPlayer(pl)).thenReturn(tkn);
    Assert.assertEquals("MY_TOKEN", tkn.getToken());
  }

  @Test(expected = IncorrectUsernameOrPwdException.class)
  public void loginPlayerShould_ThrowIncorrectUsernameOrPwdExceptionWhenNotExistingUser(){
    Mockito.when(playerService.findByUsernameAndPassword("ABC","EFG"))
            .thenReturn(null);
    playerService.loginPlayer(rqst);
  }

  @Test(expected = NotVerifiedRegistrationException.class)
  public void loginPlayerShould_ThrowNotVerifiedRegistrationExceptionWhenNotVerified() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");
    PlayerEntity pl = PlayerFactory.createPlayer(1L,null);
    pl.setIsAccountVerified(false);
    Mockito.when(playerService.findByUsernameAndPassword(rqst.getUsername(),rqst.getPassword()))
            .thenReturn(pl);
    playerService.loginPlayer(rqst);

  }

}