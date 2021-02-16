package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.controllers.TroopController;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;

public class TroopControllerUnitTest {

  private TroopController troopController;
  private TroopService troopService;

  @Before
  public void setUp() {
    troopService = Mockito.mock(TroopService.class);
    troopController = new TroopController(troopService);
  }

  @Test
  public void getKingdomTroops_returnsCorrectStatus_AndBodySize() {

    List<TroopEntityResponseDTO> dtos = new ArrayList<>();
    dtos.add(new TroopEntityResponseDTO(1,1, 100, 50, 20, 999, 1111));
    dtos.add(new TroopEntityResponseDTO(2,1, 100, 50, 20, 1111, 1222));

    Mockito.when(troopService.troopEntitiesConvertToResponseDTO( new KingdomEntity(1L)))
            .thenReturn(new TroopListResponseDto(dtos));

    ResponseEntity<TroopListResponseDto> response = troopController.getTroopsOfKingdom(createAuth("test", 1L));

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(2, response.getBody().getTroops().size());
  }
}