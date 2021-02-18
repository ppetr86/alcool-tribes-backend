package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
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
import static org.junit.Assert.*;

public class KingdomControllerUnitTest {

  private KingdomController kingdomController;
  private ResourceService resourceService;


  @Before
  public void setUp() throws Exception {
    resourceService = Mockito.mock(ResourceService.class);
    kingdomController = new KingdomController(resourceService);
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
}