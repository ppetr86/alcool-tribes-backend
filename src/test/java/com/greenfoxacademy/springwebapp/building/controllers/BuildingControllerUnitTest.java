package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;

public class BuildingControllerUnitTest {

  private BuildingController buildingController;
  private BuildingService buildingService;
  private KingdomService kingdomService;
  private ResourceService resourceService;
  private Authentication authentication;

  @Before
  public void setUp() {
    authentication = createAuth("test", 1L);

    buildingService = Mockito.mock(BuildingService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    resourceService = Mockito.mock(ResourceService.class);
    buildingController = new BuildingController(buildingService, kingdomService);
  }

  @Test
  public void getKingdomBuildings_ReturnsCorrectStatusCode() {
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 1L, 2L, null));
    Mockito.when(buildingService.findBuildingsByKingdomId(1L)).thenReturn(fakeList);

    ResponseEntity<?> response = buildingController.getKingdomBuildings(createAuth("test", 1L));

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void buildBuildings_BuildingCreated_Ok() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    ResponseEntity<?> response = buildingController.buildBuilding(createAuth("test", 1L), request);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getBuildingByIdShouldShowTheGivenBuildingDetails() {
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 100L, 200L, null);
    KingdomEntity kingdomEntity = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);
    BuildingDetailsDTO buildingDetailsDTO = new BuildingDetailsDTO();
    buildingDetailsDTO.setType(buildingEntity.getType().toString().toLowerCase());
    buildingDetailsDTO.setLevel(buildingEntity.getLevel());
    buildingDetailsDTO.setHp(buildingEntity.getHp());

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildingEntity);
    Mockito.when(buildingService.kingdomHasThisBuilding(kingdomEntity, buildingEntity)).thenReturn(true);
    Mockito.when(buildingService.showActualBuildingDetails(kingdomEntity, 1L)).thenReturn(buildingDetailsDTO);
    Mockito.when(kingdomService.findByID(kingdomEntity.getId())).thenReturn(kingdomEntity);

    ResponseEntity<?> response = buildingController.getBuildingById(1L, authentication);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("farm", ((BuildingDetailsDTO) response.getBody()).getType());
    Assert.assertEquals(1, ((BuildingDetailsDTO) response.getBody()).getLevel());
    Assert.assertEquals(100, ((BuildingDetailsDTO) response.getBody()).getHp());
  }
}


