package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class TroopServiceImpl implements TroopService {

  private final TroopEntityRepository repo;

  //TODO: ALTB-14
  @Override
  public Set<TroopEntity> findTroopsByKingdomID(Long id) {

    return repo.findAllByKingdomID(id);
  }


}
