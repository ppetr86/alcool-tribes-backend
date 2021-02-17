package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import org.springframework.stereotype.Service;

@Service
public class KingdomServiceImpl implements KingdomService {
  private KingdomRepository kingdomRepository;

  public KingdomServiceImpl(KingdomRepository kingdomRepository) {
    this.kingdomRepository = kingdomRepository;
  }


  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {
    return kingdomRepository.save(kingdom);
  }

  @Override
  public KingdomEntity findById(Long id) {
    return kingdomRepository.findById(id).orElse(null);
  }

}

