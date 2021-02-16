package com.greenfoxacademy.springwebapp.troop.controllers;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultLevel1BuildingsWithAllData;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopPostResponseDTO;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class TroopsControllerUnitTests {
  private TroopController troopController;
  private TroopService troopService;

  private Authentication authentication;

  @Before
  public void setUp() {
    troopService = Mockito.mock(TroopService.class);
    troopController = new TroopController(troopService);

    authentication = createAuth("Zdenek", 1L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(createDefaultLevel1BuildingsWithAllData());
  }

  @Test
  public void createTroop_troopIsCreated() {
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);
    TroopPostResponseDTO responseDTO = new TroopPostResponseDTO(1L, 1, 200, 50, 20, 10000L, 20000L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    Mockito.when(troopService.createTroop(kingdom, requestDTO)).thenReturn(responseDTO);

    ResponseEntity<?> response = troopController.createTroop(requestDTO, authentication);

    Assert.assertEquals(200, response.getStatusCodeValue());
    Assert.assertEquals(1, ((TroopPostResponseDTO) response.getBody()).getId().intValue());
    Assert.assertEquals(1, ((TroopPostResponseDTO) response.getBody()).getLevel().intValue());
    Assert.assertEquals(200, ((TroopPostResponseDTO) response.getBody()).getHp().intValue());
    Assert.assertEquals(50, ((TroopPostResponseDTO) response.getBody()).getAttack().intValue());
    Assert.assertEquals(20, ((TroopPostResponseDTO) response.getBody()).getDefence().intValue());
    Assert.assertEquals(10000, ((TroopPostResponseDTO) response.getBody()).getStartedAt().intValue());
    Assert.assertEquals(20000, ((TroopPostResponseDTO) response.getBody()).getFinishedAt().intValue());
  }
}
