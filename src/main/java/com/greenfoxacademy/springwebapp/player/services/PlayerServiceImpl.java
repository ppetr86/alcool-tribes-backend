package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.context.VerificationEmail;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.services.LocationService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepo;
  private final PasswordEncoder passwordEncoder;
  private final BuildingService buildingService;
  private final EmailService emailService;
  private final RegistrationTokenService registrationTokenService;
  private final TokenService tokenService;
  private final ResourceService resourceService;
  private final LocationService locationService;
  private final Environment env;

  @Override
  public PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request)
      throws UsernameIsTakenException {

    if (existsPlayerByUsername(request.getUsername())) {
      throw new UsernameIsTakenException();
    }

    PlayerEntity savedPlayer = saveNewPlayer(request);
    boolean mailWasSent = false;
    if (!request.getEmail().isEmpty()) {
      mailWasSent = sendRegistrationConfirmationEmail(savedPlayer);
    }
    return savedPlayer;
  }

  @Override
  public PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);
    List<BuildingEntity> defaultBuildings = buildingService.createDefaultBuildings(kingdom);
    kingdom.setBuildings(defaultBuildings);

    PlayerEntity player = copyProperties(kingdom, dto, false);
    player.setPassword(passwordEncoder.encode(dto.getPassword()));

<<<<<<< HEAD
    kingdom.setResources(resourceService.createDefaultResources(kingdom));
    LocationEntity defaultLocation = new LocationEntity(1L, 10, 10);
    locationService.save(defaultLocation);
    kingdom.setLocation(defaultLocation);
=======
    PlayerEntity player =
        new PlayerEntity(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getEmail());
    if (dto.getEmail() == null) {
      player.setEmail("");
    }
    player.setKingdom(kingdom);
>>>>>>> development
    kingdom.setPlayer(player);

    player = playerRepo.save(player);
    return player;
  }

  @Override
  public boolean sendRegistrationConfirmationEmail(PlayerEntity player) {
    RegistrationTokenEntity token = registrationTokenService.createSecureToken(player);
    token.setPlayer(player);
    registrationTokenService.saveSecureToken(token);

    VerificationEmail emailContext = new VerificationEmail();
    emailContext.init(player);
    emailContext.setToken(token.getToken());
    emailContext.buildVerificationUrl(env.getProperty("site.base.url.http"), token.getToken());

    try {
      emailService.sendMailWithHtmlAndPlainText(emailContext);
    } catch (MessagingException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private KingdomEntity assignKingdomName(PlayerRegisterRequestDTO dto) {
    KingdomEntity kingdom = new KingdomEntity();
    if (dto.getKingdomname() != null) {
      kingdom.setKingdomName(dto.getKingdomname());
    } else {
      kingdom.setKingdomName(dto.getUsername() + "'s kingdom");
    }
    return kingdom;
  }

  @Override
  public PlayerResponseDTO playerToResponseDTO(PlayerEntity playerEntity) {
    PlayerResponseDTO responseDTO = new PlayerResponseDTO();
    BeanUtils.copyProperties(playerEntity, responseDTO);
    responseDTO.setKingdomId(playerEntity.getKingdom().getId());
    return responseDTO;
  }


  @Override
  public PlayerTokenDTO loginPlayer(PlayerRequestDTO request)
      throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {

    PlayerEntity player = findByUsernameAndPassword(request.getUsername(), request.getPassword());

    if (player == null) {
      throw new IncorrectUsernameOrPwdException();
    } else if (!player.getIsAccountVerified()) {
      throw new NotVerifiedRegistrationException();
    }
    return tokenService.generateTokenToLoggedInPlayer(player);
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return playerRepo.findByUsername(username);
  }

  @Override
  public boolean existsPlayerByUsername(String username) {
    return playerRepo.existsByUsername(username);
  }

  @Override
  public PlayerEntity findByUsernameAndPassword(String username, String password) {
    PlayerEntity playerEntity = findByUsername(username);

    if (playerEntity != null) {
      if (passwordEncoder.matches(password, playerEntity.getPassword())) {
        return playerEntity;
      }
    }
    return null;
  }

  @Override
  public boolean verifyUser(String token) throws InvalidTokenException {
    RegistrationTokenEntity secureToken = registrationTokenService.findByToken(token);
    if (Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()) {
      throw new InvalidTokenException();
    }
    PlayerEntity player = playerRepo.getOne(secureToken.getPlayer().getId());
    if (Objects.isNull(player)) {
      return false;
    }

    player.setIsAccountVerified(true);
    playerRepo.save(player);

    registrationTokenService.removeToken(secureToken);
    return true;
  }

  private PlayerEntity copyProperties(KingdomEntity kingdom, PlayerRegisterRequestDTO dto, boolean verified) {
    PlayerEntity player = new PlayerEntity();
    player.setEmail(dto.getEmail());
    player.setUsername(dto.getUsername());
    player.setKingdom(kingdom);
    player.setIsAccountVerified(verified);
    return player;
  }
}