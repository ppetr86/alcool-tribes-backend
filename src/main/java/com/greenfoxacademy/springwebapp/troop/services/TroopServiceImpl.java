package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {

  private final TroopRepository repo;
  private final ModelMapper modelMapper;

  private List<TroopEntityResponseDTO> convertEntityListToDTO(List<TroopEntity> entities) {
    return entities
            .stream()
            .map(this::convertEntityToEntityResponseDTO)
            .collect(Collectors.toList());
  }

  private TroopListResponseDto convertDTOListToDTO(List<TroopEntityResponseDTO> list) {
    return new TroopListResponseDto(list);
  }

  private TroopEntityResponseDTO convertEntityToEntityResponseDTO(TroopEntity entity) {
    modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
    TroopEntityResponseDTO result = modelMapper
            .map(entity, TroopEntityResponseDTO.class);
    return result;
  }

  @Override
  public TroopListResponseDto troopEntitiesConvertToResponseDTO(KingdomEntity entity) {
    return convertDTOListToDTO(convertEntityListToDTO(entity.getTroops()));
  }
}