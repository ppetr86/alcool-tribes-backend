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

  @Override
  public TroopListResponseDto troopsToListDTO(KingdomEntity entity) {
    return new TroopListResponseDto(entity.getTroops().stream()
            .map(TroopEntityResponseDTO::new)
            .collect(Collectors.toList())
    );
  }
}