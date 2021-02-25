package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.email.context.AccountVerificationEmailContext;
import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.email.services.EmailService;
import com.greenfoxacademy.springwebapp.email.services.RegistrationTokenService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IncorrectUsernameOrPwdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotVerifiedRegistrationException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.UsernameIsTakenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${site.base.url.https}")
  private String baseURL;

  @Override
  public PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);
    List<BuildingEntity> defaultBuildings = buildingService.createDefaultBuildings(kingdom);
    kingdom.setBuildings(defaultBuildings);

    PlayerEntity player = new PlayerEntity();
    BeanUtils.copyProperties(dto, player);
    player.setPassword(passwordEncoder.encode(dto.getPassword()));
    player.setKingdom(kingdom);
    player.setIsAccountVerified(false);
    kingdom.setPlayer(player);

    playerRepo.saveAndFlush(player);
    return playerRepo.findByUsername(player.getUsername());
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
  public PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request)
          throws UsernameIsTakenException {

    if (existsByUsername(request.getUsername())) throw new UsernameIsTakenException();

    PlayerEntity savedPlayer = saveNewPlayer(request);
    if (!request.getEmail().isEmpty()) sendRegistrationConfirmationEmail(savedPlayer);
    return savedPlayer;
  }

  @Override
  public PlayerTokenDTO loginPlayer(PlayerRequestDTO request) throws IncorrectUsernameOrPwdException, NotVerifiedRegistrationException {

    PlayerEntity player = findByUsernameAndPassword(request.getUsername(), request.getPassword());

    if (player==null)
      throw new IncorrectUsernameOrPwdException();
    else if (!player.getIsAccountVerified())
      throw new NotVerifiedRegistrationException();
    return tokenService.generateTokenToLoggedInPlayer(player);
  }

  @Override
  public PlayerEntity findByUsername(String username) {
    return playerRepo.findByUsername(username);
  }

  @Override
  public boolean existsByUsername(String username) {
    return playerRepo.existsByUsername(username);
  }

  @Override
  public void sendRegistrationConfirmationEmail(PlayerEntity player) {
    RegistrationTokenEntity secureToken = registrationTokenService.createSecureToken();
    secureToken.setPlayer(player);
    registrationTokenService.saveSecureToken(secureToken);

    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
    emailContext.init(player);
    emailContext.setToken(secureToken.getToken());
    emailContext.buildVerificationUrl(baseURL, secureToken.getToken());

    try {
      emailService.sendMailWithHtmlAndPlainText(emailContext);
      emailService.sendTextEmail(emailContext);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
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
  public boolean findIsVerified(String username) {
    return playerRepo.isVerifiedUsername(username);
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
}