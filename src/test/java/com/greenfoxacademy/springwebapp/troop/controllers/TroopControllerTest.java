package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;

public class TroopControllerTest {

  private TroopController troopController;
  private TroopService troopService;

  @Before
  public void setUp() {
    troopService = Mockito.mock(TroopService.class);
    troopController = new TroopController(troopService);
  }

  @Test
  public void getKingdomTroops_returnsCorrectStatus_AndBodySize() {

    KingdomEntity ke = new KingdomEntity();
    ke.setTroops(TroopFactory.createDefaultTroops());
    List<TroopEntityResponseDTO> list = ke.getTroops()
            .stream()
            .map(TroopEntityResponseDTO::new)
            .collect(Collectors.toList());

    Mockito.when(troopService.troopsToListDTO(KingdomFactory.createKingdomEntityWithId(1l)))
            .thenReturn(new TroopListResponseDto(list));

    ResponseEntity<TroopListResponseDto> response = troopController.getTroopsOfKingdom(createAuth("test", 1L));

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(3, response.getBody().getTroops().size());
  }
}