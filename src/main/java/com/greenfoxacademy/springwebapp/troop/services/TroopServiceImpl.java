package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopEntityRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {

  private final TroopEntityRepository repo;
  private final ModelMapper modelMapper;

  @Override
  public List<TroopEntity> findTroopsByKingdom(KingdomEntity entity) {
    //TODO: needs to implement the Kingdom Entity
    return repo.findAllByKingdom(entity);
  }

  public List<TroopEntityResponseDTO> convertEntityListToDTO(List<TroopEntity> entities) {
    return entities
            .stream()
            .map(this::convertEntityToEntityResponseDTO)
            .collect(Collectors.toList());
  }

  public TroopResponseDto convertDTOListToDTO(List<TroopEntityResponseDTO> list) {
    return new TroopResponseDto(list);
  }

  @Override
  public TroopEntityResponseDTO convertEntityToEntityResponseDTO(TroopEntity entity) {
    modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
    TroopEntityResponseDTO result = modelMapper
            .map(entity, TroopEntityResponseDTO.class);
    return result;
  }

  @Override
  public TroopResponseDto findTroopEntitiesConvertToResponseDTO(KingdomEntity entity) {
    List<TroopEntity> troopEntities = findTroopsByKingdom(entity);
    List<TroopEntityResponseDTO> entityResponseDTOs = convertEntityListToDTO(troopEntities);
    return convertDTOListToDTO(entityResponseDTOs);
  }
}