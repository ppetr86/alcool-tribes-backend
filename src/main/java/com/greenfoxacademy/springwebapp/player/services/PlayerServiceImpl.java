package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.configuration.email.EmailService;
import com.greenfoxacademy.springwebapp.configuration.email.SecureToken;
import com.greenfoxacademy.springwebapp.configuration.email.SecureTokenService;
import com.greenfoxacademy.springwebapp.configuration.email.context.AccountVerificationEmailContext;
import com.greenfoxacademy.springwebapp.configuration.email.repository.SecureTokenRepository;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.repositories.PlayerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

@Service
public class PlayerServiceImpl implements PlayerService {

  private final PlayerRepository playerRepo;
  private final PasswordEncoder passwordEncoder;
  private final BuildingService buildingService;
  private final EmailService emailService;
  private final SecureTokenService secureTokenService;
  private final SecureTokenRepository secureTokenRepository;

  @Value("${site.base.url.https}")
  private String baseURL;

  public PlayerServiceImpl(PlayerRepository playerRepo, PasswordEncoder passwordEncoder, BuildingService buildingService, EmailService emailService, SecureTokenService secureTokenService, SecureTokenRepository secureTokenRepository) {
    this.playerRepo = playerRepo;
    this.passwordEncoder = passwordEncoder;
    this.buildingService = buildingService;
    this.emailService = emailService;
    this.secureTokenService = secureTokenService;
    this.secureTokenRepository = secureTokenRepository;
  }

  @Override
  public PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO dto) {
    KingdomEntity kingdom = assignKingdomName(dto);
    List<BuildingEntity> defaultBuildings = buildingService.createDefaultBuildings(kingdom);
    kingdom.setBuildings(defaultBuildings);

    PlayerEntity player = new PlayerEntity();
    BeanUtils.copyProperties(dto,player);
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
    BeanUtils.copyProperties(playerEntity,responseDTO);
    responseDTO.setKingdomId(playerEntity.getKingdom().getId());
    return responseDTO;
  }

  @Override
  public PlayerEntity findByUsername(String username) {

    return playerRepo.findByUsername(username);
  }

  @Override
  public void sendRegistrationConfirmationEmail(PlayerEntity user) {
    SecureToken secureToken= secureTokenService.createSecureToken();
    secureToken.setPlayer(user);
    secureTokenRepository.save(secureToken);
    AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
    emailContext.init(user);
    emailContext.setToken(secureToken.getToken());
    emailContext.buildVerificationUrl(baseURL, secureToken.getToken());
    try {
      //emailService.sendMail(emailContext, user.getUsername(), user.getKingdom().getKingdomName(), user.getEmail());
      emailService.sendTextEmail(emailContext, user.getUsername(), user.getKingdom().getKingdomName(), user.getEmail());
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
  public void updateIsVerifiedOnPlayer(long playerID, boolean isVerified) {
    playerRepo.updateIsVefifiedOnPlayer(playerID, isVerified);
  }

  @Override
  public Long getPlayerIDFromUsername(String username) {
    return playerRepo.findPlayerIDByUsername(username);
  }

  @Override
  public boolean verifyUser(String token) throws InvalidTokenException {
    SecureToken secureToken = secureTokenService.findByToken(token);
    if(Objects.isNull(secureToken) || !StringUtils.equals(token, secureToken.getToken()) || secureToken.isExpired()){
      throw new InvalidTokenException("Token is not valid");
    }
    PlayerEntity user = playerRepo.getOne(secureToken.getPlayer().getId());
    if(Objects.isNull(user)){
      return false;
    }
    user.setIsAccountVerified(true);
    playerRepo.save(user); // let's same user details

    // we don't need invalid password now
    secureTokenService.removeToken(secureToken);
    return true;
  }


}