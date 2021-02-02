package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;

import java.util.Set;

public interface TroopService {

  Set<TroopEntity> findTroopsByKingdomID(Long id);

  Set<TroopEntityResponseDto> convertEntitySetToDTO(Set<TroopEntity> entities);

  TroopResponseDto convertDTOSetToDTO(Set<TroopEntityResponseDto> set);

  TroopEntityResponseDto convertEntityToEntityResponseDTO(TroopEntity entity);

  TroopResponseDto findTroopEntitiesConvertToResponseDTO(Long id);


}
