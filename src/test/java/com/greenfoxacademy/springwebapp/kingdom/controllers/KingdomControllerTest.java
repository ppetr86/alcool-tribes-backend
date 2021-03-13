package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleRequestDTO;
import com.greenfoxacademy.springwebapp.battle.models.dtos.BattleResponseDTO;
import com.greenfoxacademy.springwebapp.battle.services.BattleService;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;

public class KingdomControllerTest {

  private KingdomController kingdomController;
  private KingdomService kingdomService;
  private ResourceService resourceService;
  private KingdomRepository kingdomRepository;
  private Authentication authentication;
  private BattleService battleService;

  @Before
  public void setUp() {
    authentication = createAuth("test", 1L);
    kingdomRepository = Mockito.mock(KingdomRepository.class);
    resourceService = Mockito.mock(ResourceService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    battleService = Mockito.mock(BattleService.class);
    kingdomController = new KingdomController(kingdomService, resourceService, battleService);

    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("testKingdom");
    kingdom.setId(1L);

    PlayerEntity pl = PlayerFactory.createPlayer(1L, kingdom);

    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(1L);

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(kingdomService.entityToKingdomResponseDTO(1L)).thenReturn(result);
  }

  @Test
  public void getKingdomResourcesShouldReturnCorrectStatusCode() {
    ResponseEntity<?> response = kingdomController.getKingdomResources(createAuth("testKingdom", 1L));
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void existingKingdomReturns200Status() {
    ResponseEntity<Object> response = kingdomController.getKingdomByID(1L);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test(expected = IdNotFoundException.class)
  public void non_existingKingdomReturns400_AndRelevantResponse() {

    Mockito.when(kingdomService.entityToKingdomResponseDTO(1111L)).thenThrow(IdNotFoundException.class);

    ResponseEntity<Object> response = kingdomController.getKingdomByID(1111L);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void updateKingdomByNameShouldReturnUpdatedBuilding() {
    KingdomNameDTO nameDTO = new KingdomNameDTO("New Kingdom");
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setId(1L);
    kingdom.setKingdomName(nameDTO.getName());
    KingdomResponseDTO responseDTO = new KingdomResponseDTO();
    responseDTO.setId(kingdom.getId());
    responseDTO.setName(kingdom.getKingdomName());

    Mockito.when(kingdomService.changeKingdomName(kingdom, nameDTO)).thenReturn(responseDTO);

    ResponseEntity<?> response = kingdomController.updateKingdomByName(authentication, nameDTO);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(1, ((KingdomResponseDTO) response.getBody()).getId());
    Assert.assertEquals("New Kingdom", ((KingdomResponseDTO) response.getBody()).getName());
  }

  @Test(expected = MissingParameterException.class)
  public void updateKingdomByNameShouldReturnMissingParameterExceptionIfDtoIsEmpty() {
    KingdomNameDTO nameDTO = new KingdomNameDTO("");
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    Mockito.when(kingdomService.changeKingdomName(kingdom, nameDTO)).thenThrow(MissingParameterException.class);

    ResponseEntity<?> response = kingdomController.updateKingdomByName(authentication, nameDTO);
  }

  @Test(expected = MissingParameterException.class)
  public void updateKingdomByNameShouldReturnMissingParameterExceptionIfDtoIsNull() {
    KingdomNameDTO nameDTO = new KingdomNameDTO();
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    Mockito.when(kingdomService.changeKingdomName(kingdom, nameDTO)).thenThrow(MissingParameterException.class);

    ResponseEntity<?> response = kingdomController.updateKingdomByName(authentication, nameDTO);
  }

  @Test
  public void inititateBattleShouldReturnProperResponseDTO() {
    Long[] troopIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopIds);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setTroops(TroopFactory.createDefaultTroops());

    Mockito.when(battleService.goToWar(2L,requestDTO,kingdom,1)).thenReturn(new BattleResponseDTO());

    ResponseEntity<?> response = kingdomController.initiateBattle(2L, requestDTO, authentication);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("ok", ((BattleResponseDTO) response.getBody()).getStatus());
    Assert.assertEquals("Battle started", ((BattleResponseDTO) response.getBody()).getMessage());
  }

  @Test(expected = MissingParameterException.class)
  public void inititateBattleShouldReturnMissingParameterException() {
    Long[] troopIds = {10L,20L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopIds);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setTroops(TroopFactory.createDefaultTroops());

    Mockito.when(battleService.goToWar(2L,requestDTO,kingdom,1))
        .thenThrow(new MissingParameterException("anything"));

    ResponseEntity<?> response = kingdomController.initiateBattle(2L, requestDTO, authentication);
  }

  @Test(expected = IdNotFoundException.class)
  public void inititateBattleShouldReturnIdNotFoundException() {
    Long[] troopIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopIds);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setTroops(TroopFactory.createDefaultTroops());

    Mockito.when(battleService.goToWar(20L,requestDTO,kingdom,1))
        .thenThrow(new IdNotFoundException());

    ResponseEntity<?> response = kingdomController.initiateBattle(20L, requestDTO, authentication);
  }

  @Test(expected = ForbiddenActionException.class)
  public void inititateBattleShouldReturnForbiddenActionException() {
    Long[] troopIds = {1L,2L};
    BattleRequestDTO requestDTO = new BattleRequestDTO(troopIds);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setTroops(TroopFactory.createDefaultTroops());

    Mockito.when(battleService.goToWar(1L,requestDTO,kingdom,1))
        .thenThrow(new ForbiddenActionException());

    ResponseEntity<?> response = kingdomController.initiateBattle(1L, requestDTO, authentication);
  }
}