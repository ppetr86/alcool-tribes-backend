package com.greenfoxacademy.springwebapp.buildings;

import com.greenfoxacademy.springwebapp.buildings.controllers.BuildingsController;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidBuildingTypeException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdoms.services.KingdomService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BuildingsControllerUnitTest {

  private BuildingsController buildingController;
  private BuildingService buildingService;
  private KingdomService kingdomService;

  @Before
  public void setUp() {
    buildingService = Mockito.mock(BuildingService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    buildingController = new BuildingsController(buildingService, kingdomService);
  }

  @Test
  public void buildBuildings_EmptyInput_BadRequest() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO(" ");
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void buildBuildings_WrongType_NotAcceptable() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO("faaarm");
    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_WrongType_NotAcceptableV2() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO("helloo");
    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_NoTownhall_NotAcceptable() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_LowResource_NotAcceptable() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(kingdomService.hasResourcesForBuilding()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void buildBuildings_BuildingCreated_Ok() throws MissingParameterException, InvalidBuildingTypeException, NotEnoughResourceException {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(kingdomService.hasResourcesForBuilding()).thenReturn(true);
    ResponseEntity<?> response = buildingController.buildBuilding(request);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
