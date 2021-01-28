package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KingdomServiceImpl implements KingdomService {

  private final KingdomRepository repo;

  @Override
  public boolean hasKingdomTownhall() {
    //TODO: hasKingdomTownhall
    return false;
  }

  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }
}