package com.greenfoxacademy.springwebapp.troop.controllers;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultLevel1BuildingsWithAllData;


import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class TroopControllerUnitTest {
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
    TroopEntityResponseDTO
        responseDTO = new TroopEntityResponseDTO(1L, 1, 200, 50, 20, 10000L, 20000L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    Mockito.when(troopService.createTroop(kingdom, requestDTO)).thenReturn(responseDTO);

    ResponseEntity<?> response = troopController.createTroop(requestDTO, authentication);

    Assert.assertEquals(200, response.getStatusCodeValue());
    Assert.assertEquals(1L, ((TroopEntityResponseDTO) response.getBody()).getId().longValue());
    Assert.assertEquals(1, ((TroopEntityResponseDTO) response.getBody()).getLevel());
    Assert.assertEquals(200, ((TroopEntityResponseDTO) response.getBody()).getHp());
    Assert.assertEquals(50, ((TroopEntityResponseDTO) response.getBody()).getAttack());
    Assert.assertEquals(20, ((TroopEntityResponseDTO) response.getBody()).getDefence());
    Assert.assertEquals(10000, ((TroopEntityResponseDTO) response.getBody()).getStartedAt());
    Assert.assertEquals(20000, ((TroopEntityResponseDTO) response.getBody()).getFinishedAt());
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