package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.repositories.KingdomEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KingdomServiceImpl implements KingdomService {

  private final KingdomEntityRepository repo;

  @Override
  public boolean hasKingdomTownhall() {
    //TODO: hasKingdomTownhall
    return true;
  }

  //TODO: ALTB-14
  @Override
  public KingdomEntity findByID(Long id) {
    return repo.findById(id).orElse(null);
  }
}
