package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.controllers.TroopController;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Set;

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

    Set<TroopEntityResponseDto> dtos = new HashSet<>();
    dtos.add(new TroopEntityResponseDto(1, 100, 50, 20, 999, 1111));
    dtos.add(new TroopEntityResponseDto(1, 100, 50, 20, 1111, 1222));

    Mockito.when(troopService.findTroopEntitiesConvertToResponseDTO(1L))
            .thenReturn(new TroopResponseDto(dtos));
    ResponseEntity<TroopResponseDto> response = troopController.getTroopsOfKingdom(1L);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(2, response.getBody().getTroops().size());
  }
}
