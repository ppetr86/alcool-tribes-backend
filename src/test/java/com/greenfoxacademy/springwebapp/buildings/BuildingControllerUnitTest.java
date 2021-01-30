package com.greenfoxacademy.springwebapp.buildings;

import com.greenfoxacademy.springwebapp.buildings.controllers.BuildingsController;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

public class BuildingControllerUnitTest {

  private BuildingsController buildingController;
  private BuildingService buildingService;
  private KingdomService kingdomService;
  private ResourceService resourceService;
  private TimeService timeService;

  @Before
  public void setUp() {
    buildingService = Mockito.mock(BuildingService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    buildingController = new BuildingsController(buildingService, kingdomService,timeService,resourceService);
  }

  @Test
  public void buildBuildings_EmptyInput_BadRequest() {
    BuildingRequestDTO request = new BuildingRequestDTO(" ");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");
    ResponseEntity<?> response = buildingController.buildBuilding(request,bindingResult);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void buildBuildings_WrongType_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("faaarm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request,bindingResult);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_NoTownhall_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request,bindingResult);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_LowResource_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(request,bindingResult);
    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void buildBuildings_BuildingCreated_Ok() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    ResponseEntity<?> response = buildingController.buildBuilding(request,bindingResult);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
