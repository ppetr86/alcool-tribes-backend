package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;

  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {
    return kingdomRepository.save(kingdom);
  }
}