package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.context.AccountVerificationEmail;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

public class PlayerServiceTest {

  private PlayerService playerService;

  private PlayerRepository playerRepository;
  private PasswordEncoder passwordEncoder;
  private BuildingService buildingService;
  private EmailService emailService;
  private RegistrationTokenService registrationTokenService;
  private TokenService tokenService;
  private AccountVerificationEmail accountVerification;
  private ResourceService resourceService;

  @Before
  public void setUp() {
    accountVerification = Mockito.mock(AccountVerificationEmail.class);
    playerRepository = Mockito.mock(PlayerRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    buildingService = Mockito.mock(BuildingService.class);
    emailService = Mockito.mock(EmailService.class);
    registrationTokenService = Mockito.mock(RegistrationTokenService.class);
    tokenService = Mockito.mock(TokenService.class);
    resourceService = Mockito.mock(ResourceService.class);
    playerService = new PlayerServiceImpl(playerRepository, passwordEncoder, buildingService, emailService, registrationTokenService, tokenService,resourceService);
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
  public void loginPlayerShouldReturnPlayerRequestDTO() {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null);
    Mockito.when(playerService.findByUsernameAndPassword(rqst.getUsername(), rqst.getPassword()))
            .thenReturn(pl);
    PlayerTokenDTO tkn = new PlayerTokenDTO("MY_TOKEN");
    Mockito.when(tokenService.generateTokenToLoggedInPlayer(pl)).thenReturn(tkn);
    Assert.assertEquals("MY_TOKEN", tkn.getToken());
  }

  @Test(expected = IncorrectUsernameOrPwdException.class)
  public void loginPlayerShould_ThrowIncorrectUsernameOrPwdExceptionWhenNotExistingUser() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");

    Mockito.when(playerService.findByUsernameAndPassword("ABC", "EFG"))
            .thenReturn(null);
    playerService.loginPlayer(rqst);
  }

  @Test(expected = NotVerifiedRegistrationException.class)
  public void loginPlayerShould_ThrowNotVerifiedRegistrationExceptionWhenNotVerified() throws NotVerifiedRegistrationException, IncorrectUsernameOrPwdException {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false);
    Mockito.when(playerRepository.findByUsername(rqst.getUsername()))
            .thenReturn(pl);
    Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(true);
    playerService.loginPlayer(rqst);
  }

  @Test
  public void sendRegistrationConfirmationEmail_ReturnsTrue() {
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");

    KingdomEntity ke = KingdomFactory.createFullKingdom(1L,1L);
    PlayerEntity pl = ke.getPlayer();
    pl.setIsAccountVerified(false);
    pl.setUsername("Dezo");
    AccountVerificationEmail emailContext = new AccountVerificationEmail();
    emailContext.init(pl);
    emailContext.setToken(secureToken.getToken());
    emailContext.buildVerificationUrl("http://localhost:8080", secureToken.getToken());

    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));

    Mockito.when(registrationTokenService.createSecureToken()).thenReturn(secureToken);
    Mockito.when(playerService.sendRegistrationConfirmationEmail(pl)).thenReturn(true);
    Assert.assertFalse(playerService.sendRegistrationConfirmationEmail(pl));
  }

  @Test
  public void sendRegistrationConfirmationEmail_ReturnsFalse() throws MessagingException {
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");

    KingdomEntity ke = KingdomFactory.createFullKingdom(1L,1L);
    PlayerEntity pl = ke.getPlayer();
    pl.setIsAccountVerified(false);
    pl.setUsername("Dezo");

    AccountVerificationEmail emailContext = new AccountVerificationEmail();
    emailContext.init(pl);
    emailContext.setToken(secureToken.getToken());

    accountVerification.buildVerificationUrl("http://localhost:8080", secureToken.getToken());
    emailContext.put("verificationURL","http://localhost:8080/register/verify?token="+secureToken.getToken());

    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));

    Mockito.when(registrationTokenService.createSecureToken()).thenReturn(secureToken);

    Assert.assertFalse(playerService.sendRegistrationConfirmationEmail(pl));
  }

  @Test
  public void findIsVerified_returnsExpectedResult_False() {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false, "Superman");
    Mockito.when(playerRepository.isVerifiedUsername("Superman")).thenReturn(false);
    Assert.assertFalse(pl.getIsAccountVerified());
  }

  @Test
  public void findIsVerified_returnsExpectedResult_True() {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, true, "Superman");
    Mockito.when(playerRepository.isVerifiedUsername("Superman")).thenReturn(true);
    Assert.assertTrue(pl.getIsAccountVerified());
  }

  @Test
  public void findByUsernameAndPassword_retunsNullOnNoMatch() {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, true, "Superman");
    Mockito.when(passwordEncoder.matches("passwordD", "123456789")).thenReturn(false);
    Assert.assertNull(playerService.findByUsernameAndPassword("Batman", "123456789"));
  }

  @Test
  public void findByUsernameAndPassword_retunsPlayerOnMatch() {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, true, "Superman");
    Mockito.when(playerService.findByUsername("Superman")).thenReturn(pl);
    Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(true);
    Assert.assertEquals(pl, playerService.findByUsernameAndPassword("Superman", "password"));
  }

  @Test(expected = InvalidTokenException.class)
  public void verifyUser_ThrowsInvalidTokenException_NullToken() throws InvalidTokenException {
    String token = null;
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(null);
    playerService.verifyUser(token);
  }

  @Test(expected = InvalidTokenException.class)
  public void verifyUser_ThrowsInvalidTokenException_NoMatchOfToken() throws InvalidTokenException {
    String token = "123";
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123456789");
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    playerService.verifyUser(token);
  }

  @Test(expected = InvalidTokenException.class)
  public void verifyUser_ThrowsInvalidTokenException_TokenExpired() throws InvalidTokenException {
    String token = "123";
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123456789");
    secureToken.setIsExpired(true);
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    playerService.verifyUser(token);
  }

  @Test
  public void verifyUser_ReturnsFalse_PlayerDoesNotExist() throws InvalidTokenException {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false, "Superman");
    String token = "123";
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");
    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    Mockito.when(playerRepository.getOne(secureToken.getPlayer().getId())).thenReturn(null);
    Assert.assertFalse(playerService.verifyUser("123"));
  }

  @Test
  public void verifyUser_ReturnsTrue_OnTokenAndPlayerMatch() throws InvalidTokenException {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false, "Superman");
    String token = "123";
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");
    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    Mockito.when(playerRepository.getOne(secureToken.getPlayer().getId())).thenReturn(pl);
    playerService.verifyUser(token);
    Assert.assertTrue(playerService.verifyUser("123"));
  }
}