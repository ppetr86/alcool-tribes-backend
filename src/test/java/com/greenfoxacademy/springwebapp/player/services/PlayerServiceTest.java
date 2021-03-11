package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.TestConfig;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.factories.RegistrationTokenFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.services.LocationService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;

public class PlayerServiceTest {

  ResourceService resourceService;
  LocationService locationService;
  PlayerRepository playerRepository;
  PasswordEncoder passwordEncoder;
  BuildingService buildingService;
  EmailService emailService;
  RegistrationTokenService registrationTokenService;
  TokenService tokenService;
  Environment mockEnvironment;
  private PlayerServiceImpl playerService;

  @Before
  public void setUp() {
    resourceService = Mockito.mock(ResourceService.class);
    locationService = Mockito.mock(LocationService.class);
    playerRepository = Mockito.mock(PlayerRepository.class);
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    buildingService = Mockito.mock(BuildingService.class);
    emailService = Mockito.mock(EmailService.class);
    registrationTokenService = Mockito.mock(RegistrationTokenService.class);
    tokenService = Mockito.mock(TokenService.class);
    mockEnvironment = TestConfig.mockEnvironment();
    playerService = new PlayerServiceImpl(playerRepository, passwordEncoder, buildingService, emailService,
        registrationTokenService, tokenService, resourceService, locationService, mockEnvironment);
  }

  @Test
  public void saveNewPlayer_savesWithCorrectData() {
    playerService = Mockito.spy(playerService);
    PlayerRegisterRequestDTO rqst =
        new PlayerRegisterRequestDTO("testUser", "password", "test@test.com", "mycoolEmpire");
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L, false, rqst);
    Mockito.doReturn(kingdom).when(playerService).createNewEmptyKingdom();
    Mockito.when(buildingService.createDefaultBuildings(kingdom)).thenReturn(kingdom.getBuildings());
    Mockito.when(passwordEncoder.encode(rqst.getPassword())).thenReturn("hashedPWD");
    Mockito.when(resourceService.createDefaultResources(kingdom)).thenReturn(kingdom.getResources());
    Mockito.doReturn(kingdom.getPlayer()).when(playerService).copyProperties(kingdom, rqst, false);
    Mockito.when(locationService.defaultLocation(kingdom))
        .thenReturn(new LocationEntity(1L, 10, 10, kingdom, LocationType.KINGDOM));
    Mockito.when(playerRepository.save(kingdom.getPlayer())).thenReturn(kingdom.getPlayer());
    PlayerEntity player = playerService.saveNewPlayer(rqst);

    Assert.assertEquals("mycoolEmpire", player.getKingdom().getKingdomName());
    Assert.assertEquals("testUser", player.getUsername());
    Assert.assertEquals(4, player.getKingdom().getBuildings().size());
    Assert.assertEquals(100, (int) player.getKingdom().getResources().get(0).getAmount());
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

  @Test(expected = RuntimeException.class)
  public void loginPlayerShould_ThrowIncorrectUsernameOrPwdExceptionWhenNotExistingUser()
      throws RuntimeException {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");

    Mockito.when(playerService.findByUsernameAndPassword("ABC", "EFG"))
        .thenReturn(null);
    playerService.loginPlayer(rqst);
  }

  @Test(expected = RuntimeException.class)
  public void loginPlayerShould_ThrowNotVerifiedRegistrationExceptionWhenNotVerified()
      throws RuntimeException {
    PlayerRequestDTO rqst = new PlayerRequestDTO("Petr", "password");
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false);
    Mockito.when(playerRepository.findByUsername(rqst.getUsername()))
        .thenReturn(pl);
    Mockito.when(passwordEncoder.matches("password", "password")).thenReturn(true);
    playerService.loginPlayer(rqst);
  }

  @Test
  public void sendRegistrationConfirmationEmail_ReturnsTrue() {
    KingdomEntity ke = KingdomFactory.createFullKingdom(1L, 1L, false);
    RegistrationTokenEntity secureToken = RegistrationTokenFactory.createToken(ke.getPlayer());

    Mockito.when(mockEnvironment.getProperty("site.base.url.http")).thenReturn("http://localhost:8080");
    Mockito.when(registrationTokenService.createSecureToken(any())).thenReturn(secureToken);
    Assert.assertTrue(playerService.sendRegistrationConfirmationEmail(ke.getPlayer()));
  }

  @Test
  public void sendRegistrationConfirmationEmail_ReturnsFalse() throws MessagingException, IOException {
    KingdomEntity ke = KingdomFactory.createFullKingdom(1L, 1L, false);
    RegistrationTokenEntity secureToken = RegistrationTokenFactory.createToken(ke.getPlayer());

    Mockito.when(mockEnvironment.getProperty("site.base.url.http")).thenReturn("http://localhost:8080");
    Mockito.when(registrationTokenService.createSecureToken(ke.getPlayer())).thenReturn(secureToken);
    Mockito.when(registrationTokenService.createSecureToken(any())).thenReturn(secureToken);
    Mockito.when(emailService.sendMailWithHtmlAndPlainText(any())).thenThrow(new MessagingException());

    Assert.assertFalse(playerService.sendRegistrationConfirmationEmail(ke.getPlayer()));
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

    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");
    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));
    String token = "123";
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    Mockito.when(playerRepository.getOne(secureToken.getPlayer().getId())).thenReturn(null);
    Assert.assertFalse(playerService.verifyUser("123"));
  }

  @Test
  public void verifyUser_ReturnsTrue_OnTokenAndPlayerMatch() throws InvalidTokenException {
    PlayerEntity pl = PlayerFactory.createPlayer(1L, null, false, "Superman");
    RegistrationTokenEntity secureToken = new RegistrationTokenEntity();
    secureToken.setToken("123");
    secureToken.setIsExpired(false);
    secureToken.setPlayer(pl);
    secureToken.setExpireAt(LocalDateTime.now().plusDays(1));
    String token = "123";
    Mockito.when(registrationTokenService.findByToken(token)).thenReturn(secureToken);
    Mockito.when(playerRepository.getOne(secureToken.getPlayer().getId())).thenReturn(pl);
    playerService.verifyUser(token);
    Assert.assertTrue(playerService.verifyUser("123"));
  }
}