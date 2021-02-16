package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import com.greenfoxacademy.springwebapp.troop.services.TroopServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class TroopServiceTests {

  private TroopService troopService;

  @Before
  public void init(){
    troopService = new TroopServiceImpl();
  }


  @Test
  public void troopsToListDTO_ReturnsCorrectResult() {
    KingdomEntity ke = TroopFactory.createDefaultKingdomWithTroops();

    TroopListResponseDto result = troopService.troopsToListDTO(ke);

    Assert.assertEquals(3, result.getTroops().size());
    Assert.assertEquals(101, result.getTroops().get(0).getFinishedAt());
  }
}