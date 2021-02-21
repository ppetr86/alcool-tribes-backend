package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenCustomException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;

public interface TroopService {

  TroopListResponseDto troopsToListDTO(KingdomEntity entity);

  TroopEntityResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
      ForbiddenCustomException, InvalidAcademyIdException, NotEnoughResourceException;

  TroopEntityResponseDTO getTroop(KingdomEntity kingdom, Long troopId) throws
      ForbiddenCustomException, IdNotFoundException;

  TroopEntity findTroopById(Long id);
}
