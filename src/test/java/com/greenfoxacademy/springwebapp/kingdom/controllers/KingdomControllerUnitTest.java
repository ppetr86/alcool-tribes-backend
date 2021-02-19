package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;

public class KingdomControllerUnitTest {

  private KingdomController kingdomController;
  private KingdomService kingdomService;
  private ResourceService resourceService;

  @Before
  public void setUp() {
    kingdomService = Mockito.mock(KingdomService.class);
    kingdomController = new KingdomController(kingdomService, resourceService);

    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("testKingdom");
    kingdom.setId(1L);

    PlayerEntity pl = new PlayerEntity(1L, "testUser", "password", "test@test.com", null, null, kingdom);
    kingdom.setPlayer(pl);

    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(1L);

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(kingdomService.entityToKingdomResponseDTO(1L)).thenReturn(result);
  }

  @Test
  public void getKingdomResourcesShouldReturnCorrectStatusCode() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    kingdomEntity.setId(1L);
    ResourceEntity resourceEntityList = new ResourceEntity(1L, ResourceType.FOOD, 20, 10, 1678456L, kingdomEntity);
    List<ResourceResponseDTO> testList = new ArrayList<>();
    testList.add(new ResourceResponseDTO(resourceEntityList));
    Mockito.when(resourceService.findByKingdomId(1L)).thenReturn(testList);

    ResponseEntity<?> response = kingdomController.getKingdomResources(createAuth("test", 1L));

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getKingdomResourcesShouldReturnCorrectResourceType() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    kingdomEntity.setId(1L);
    ResourceEntity resourceEntityList = new ResourceEntity(1L, ResourceType.FOOD, 20, 10, 1678456L, kingdomEntity);
    List<ResourceResponseDTO> testList = new ArrayList<>();
    testList.add(new ResourceResponseDTO(resourceEntityList));
    Mockito.when(resourceService.findByKingdomId(1L)).thenReturn(testList);

    ResponseEntity<?> response = kingdomController.getKingdomResources(createAuth("test", 1L));

    Assert.assertEquals("food", ((ResourceListResponseDTO) response.getBody()).getResources().get(0).getType());
  }

  @Test
  public void getKingdomResourcesShouldReturnIncorrectResourceType() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    kingdomEntity.setId(1L);
    ResourceEntity resourceEntityList = new ResourceEntity(1L, ResourceType.FOOD, 20, 10, 1678456L, kingdomEntity);
    List<ResourceResponseDTO> testList = new ArrayList<>();
    testList.add(new ResourceResponseDTO(resourceEntityList));
    Mockito.when(resourceService.findByKingdomId(1L)).thenReturn(testList);

    ResponseEntity<?> response = kingdomController.getKingdomResources(createAuth("test", 1L));

    Assert.assertNotEquals("gold", ((ResourceListResponseDTO) response.getBody()).getResources().get(0).getType());
  }

  @Test
  public void existingKingdomReturns200Status() {

    ResponseEntity<Object> response = kingdomController.getKingdomByID(1L);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test(expected = IdNotFoundException.class)
  public void non_existingKingdomReturns400_AndRelevantReponse() {
    Mockito.when(kingdomService.entityToKingdomResponseDTO(1111L)).thenThrow(IdNotFoundException.class);

    ResponseEntity<Object> response = kingdomController.getKingdomByID(1111L);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO) response.getBody()).getMessage());
  }

}