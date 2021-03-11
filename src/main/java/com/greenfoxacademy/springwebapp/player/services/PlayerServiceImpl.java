package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.context.VerificationEmail;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.services.LocationService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
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
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
      throws RuntimeException {

    if (existsPlayerByUsername(request.getUsername())) {
      throw new RuntimeException("Username is already taken.");
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
    KingdomEntity kingdom = createNewEmptyKingdom();
    kingdom.setKingdomName(dto.getKingdomname() == null ? dto.getUsername() + "'s kingdom" : dto.getKingdomname());
    kingdom.setBuildings(buildingService.createDefaultBuildings(kingdom));

    PlayerEntity player = copyProperties(kingdom, dto, false);
    player.setPassword(passwordEncoder.encode(dto.getPassword()));

    kingdom.setResources(resourceService.createDefaultResources(kingdom));
    LocationEntity defaultLocation = new LocationEntity(1L, 10, 10);
    locationService.save(defaultLocation);
    kingdom.setLocation(defaultLocation);

    player.setKingdom(kingdom);
    kingdom.setPlayer(player);

    player = playerRepo.save(player);
    return player;
  }

  public KingdomEntity createNewEmptyKingdom() {
    return new KingdomEntity();
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
    } catch (MessagingException | IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
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
      throws RuntimeException {

    PlayerEntity player = findByUsernameAndPassword(request.getUsername(), request.getPassword());

    if (player == null) {
      throw new RuntimeException("Username or password is incorrect.");
    } else if (!player.getIsAccountVerified()) {
      throw new RuntimeException("Not verified username.");
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
  public PlayerListResponseDTO findPlayersAroundMe(KingdomEntity kingdom, Integer distance) {
    List<PlayerResponseDTO> playerResponseDTO;
    List<PlayerEntity> allPlayers = (List<PlayerEntity>) playerRepo.findAll();

    if (distance == null) {
      playerResponseDTO = allPlayers.stream()
          .map(this::playerToResponseDTO)
          .filter(e -> e.getKingdomId() != kingdom.getId())
          .collect(Collectors.toList());
    } else {
      List<KingdomEntity> kingdomEntities = findKingdomsWithinDistance(kingdom, allPlayers, distance);
      playerResponseDTO = kingdomEntities.stream()
          .map(e -> playerToResponseDTO(e.getPlayer()))
          .collect(Collectors.toList());
    }
    return new PlayerListResponseDTO(playerResponseDTO);
  }

  private List<KingdomEntity> findKingdomsWithinDistance(KingdomEntity kingdom, List<PlayerEntity> allPlayers,
                                                         Integer distance) {
    return allPlayers.stream()
        .map(PlayerEntity::getKingdom)
        .filter(e -> !e.getId().equals(kingdom.getId()))
        .filter(x -> isWithinGrid(kingdom, distance, x))
        .collect(Collectors.toList());
  }

  private boolean isWithinGrid(KingdomEntity thisKingdom, Integer distance, KingdomEntity otherKingdom) {
    return otherKingdom.getLocation().getX() > thisKingdom.getLocation().getX() - distance
        && otherKingdom.getLocation().getX() < thisKingdom.getLocation().getX() + distance
        && otherKingdom.getLocation().getY() > thisKingdom.getLocation().getY() - distance
        && otherKingdom.getLocation().getY() < thisKingdom.getLocation().getY() + distance;
  }

  public boolean verifyUser(String token) throws InvalidTokenException {
    RegistrationTokenEntity secureToken = registrationTokenService.findByToken(token);
    if (Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) ||
        secureToken.isExpired()) {
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