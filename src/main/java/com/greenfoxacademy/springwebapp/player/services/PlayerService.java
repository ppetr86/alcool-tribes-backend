package com.greenfoxacademy.springwebapp.player.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.DeletedPlayerDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRequestDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerTokenDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface PlayerService {

    DeletedPlayerDTO deletePlayer(Long deletedPlayerId);


    Boolean existsPlayerByIdAndIsVerified(long id, boolean isVerified);


    boolean existsPlayerByUsername(String username);


    List<Long> findAllByIsAccountVerified(boolean isAccountVerified);


    PlayerEntity findById(Long id);


    PlayerEntity findByUsername(String username);


    PlayerEntity findByUsernameAndPassword(String username, String password);


    PlayerListResponseDTO findPlayersAroundMe(KingdomEntity kingdom, Integer distance);


    PlayerTokenDTO loginPlayer(PlayerRequestDTO request)
            throws RuntimeException;


    PlayerResponseDTO playerToResponseDTO(PlayerEntity playerEntity);


    PlayerEntity registerNewPlayer(PlayerRegisterRequestDTO request) throws RuntimeException;


    PlayerEntity saveNewPlayer(PlayerRegisterRequestDTO playerRegistrationRequestDTO);


    PlayerEntity savePlayer(PlayerEntity player);


    boolean selectIsVerified(long id);


    boolean sendRegistrationConfirmationEmail(final PlayerEntity user);


    void setAccountVerified(long id, boolean isAccountVerified);


    PlayerEntity setAvatar(PlayerEntity player, MultipartFile file) throws FileStorageException,
            WrongContentTypeException;


    boolean verifyUser(final String token) throws InvalidTokenException;
}
