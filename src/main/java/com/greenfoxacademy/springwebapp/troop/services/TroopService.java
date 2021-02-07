package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;

import java.util.List;

public interface TroopService {

  List<TroopEntity> findTroopsByKingdom(KingdomEntity entity);

  List<TroopEntityResponseDTO> convertEntityListToDTO(List<TroopEntity> entities);

  TroopResponseDto convertDTOListToDTO(List<TroopEntityResponseDTO> set);

  TroopEntityResponseDTO convertEntityToEntityResponseDTO(TroopEntity entity);

  TroopResponseDto findTroopEntitiesConvertToResponseDTO(KingdomEntity entity);
}
