package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KingdomServiceImpl implements KingdomService {

  private KingdomRepository kingdomRepository;

  @Override
  public boolean hasKingdomTownhall() {
    //TODO: hasKingdomTownhall
    return true;
  }

  //TODO: ALTB-14
  @Override
  public KingdomEntity findByID(Long id) {
    return kingdomRepository.findById(id).orElse(null);
  }

  //TODO: ALTB-14
  @Override
  public KingdomResponseDTO kingdomResponseDTO(KingdomEntity kingdom) {
    return new KingdomResponseDTO(kingdom);
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {
    return kingdomRepository.save(kingdom);
  }

}
