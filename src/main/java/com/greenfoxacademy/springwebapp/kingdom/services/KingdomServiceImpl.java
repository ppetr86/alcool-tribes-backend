package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository kingdomRepository;

  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }

  @Override
  public KingdomEntity findByID(Long id) {
    return kingdomRepository.findById(id).orElse(null);
  }

  @Override
  public KingdomResponseDTO entityToKingdomResponseDTO(KingdomEntity kingdom) throws IdNotFoundException {
    if (kingdom == null) throw new IdNotFoundException();
    return new KingdomResponseDTO(kingdom);
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {
    return kingdomRepository.save(kingdom);
  }
}