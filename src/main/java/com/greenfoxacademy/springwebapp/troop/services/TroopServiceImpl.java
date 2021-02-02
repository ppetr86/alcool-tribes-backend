package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopEntityRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {

  private final TroopEntityRepository repo;
  private ModelMapper modelMapper;

  //TODO: ALTB-14 and ALTB-22
  @Override
  public Set<TroopEntity> findTroopsByKingdomID(Long id) {
    return repo.findAllByKingdomID(id);
  }

  @Override
  public Set<TroopEntityResponseDto> convertEntitySetToDTO(Set<TroopEntity> entities) {
    return entities
            .stream()
            .map(this::convertToEntityDTO)
            .collect(Collectors.toSet());
  }

  @Override
  public TroopResponseDto convertDTOSetToDTO(Set<TroopEntityResponseDto> set) {
    return new TroopResponseDto(set);
  }

  @Override
  public TroopEntityResponseDto convertToEntityDTO(TroopEntity entity) {
    modelMapper.getConfiguration()
            .setMatchingStrategy(MatchingStrategies.LOOSE);
    TroopEntityResponseDto result = modelMapper
            .map(entity, TroopEntityResponseDto.class);
    return result;
  }

  @Override
  public TroopResponseDto findTroopEntitiesConvertToResponseDTO(Long id) {
    Set<TroopEntity> troopEntities = findTroopsByKingdomID(id);
    Set<TroopEntityResponseDto> entityResponseDtos = convertEntitySetToDTO(troopEntities);
    return convertDTOSetToDTO(entityResponseDtos);
  }
}