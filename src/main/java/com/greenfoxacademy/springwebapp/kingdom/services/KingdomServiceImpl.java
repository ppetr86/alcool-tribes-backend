package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomEntityRepository;
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

  //TODO: ALTB-14
  @Override
  public KingdomResponseDTO kingdomResponseDTO(KingdomEntity kingdom) {
    return new KingdomResponseDTO(kingdom);
  }
}
