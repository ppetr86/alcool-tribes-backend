package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import java.util.List;

public interface TroopService {

  TroopListResponseDto troopsToListDTO(KingdomEntity entity);

  TroopEntityResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
      ForbiddenActionException, InvalidAcademyIdException, NotEnoughResourceException;

  TroopEntityResponseDTO updateTroopLevel(KingdomEntity kingdomEntity, TroopRequestDTO requestDTO, Long troopId) throws
      MissingParameterException, ForbiddenActionException, IdNotFoundException,
      InvalidInputException, NotEnoughResourceException;

  TroopEntityResponseDTO getTroop(KingdomEntity kingdom, Long troopId) throws
      ForbiddenActionException, IdNotFoundException;

  TroopEntity findTroopById(Long id);

  void deleteListOfTroopsByTroopsIds(List<Long> ids);

  void deleteListOfTroops(List<TroopEntity> deadTroops);
}
