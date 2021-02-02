package com.greenfoxacademy.springwebapp.kingdom.services;

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

}
