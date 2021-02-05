package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

public class BuildingControllerUnitTest {

  private BuildingsController buildingController;
  private BuildingService buildingService;
  private KingdomService kingdomService;
  private ResourceService resourceService;
  private Principal principal;

  @Before
  public void setUp() {
    principal = Mockito.mock(Principal.class);
    buildingService = Mockito.mock(BuildingService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    resourceService = Mockito.mock(ResourceService.class);
    buildingController = new BuildingsController(buildingService, kingdomService, resourceService);
  }

  @Test
  public void buildBuildings_WrongType_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("faaarm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(principal, request,bindingResult);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_NoTownhall_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(principal, request,bindingResult);
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }

  @Test
  public void buildBuildings_LowResource_NotAcceptable() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    ResponseEntity<?> response = buildingController.buildBuilding(principal, request,bindingResult);
    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void buildBuildings_BuildingCreated_Ok() {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "");

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    ResponseEntity<?> response = buildingController.buildBuilding(principal, request,bindingResult);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }


  //Mark's tests
  @Test
  public void getBuildingByIdShouldReturnOkStatus(){
    List<BuildingEntity> buildings = Arrays.asList(
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity()
    );

    Mockito.when(buildingService.countBuildings()).thenReturn((long)buildings.size());
    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildings.get(0));

    ResponseEntity<?> response = buildingController.getBuildingById(1L);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getBuildingByIdShouldReturnNotFoundStatusWithBiggerNumberThanBuildingsSize(){
    List<BuildingEntity> buildings = Arrays.asList(
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity()
    );

    Mockito.when(buildingService.countBuildings()).thenReturn((long)buildings.size());
    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildings.get(0));

    ResponseEntity<?> response = buildingController.getBuildingById(6L);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void getBuildingByIdShouldReturnNotFoundStatusWithLowerNumberThanZero(){
    List<BuildingEntity> buildings = Arrays.asList(
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity()
    );

    Mockito.when(buildingService.countBuildings()).thenReturn((long)buildings.size());
    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildings.get(0));

    ResponseEntity<?> response = buildingController.getBuildingById(-1L);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void getBuildingByIdShouldReturnForbiddenStatus(){
    List<BuildingEntity> buildings = Arrays.asList(
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity(),
      new BuildingEntity()
    );

    Mockito.when(buildingService.countBuildings()).thenReturn((long)buildings.size());
    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildings.get(0));

    ResponseEntity<?> response = buildingController.getBuildingById(2L);

    Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    Assert.assertEquals("Forbidden action", ((ErrorDTO)response.getBody()).getMessage());
  }
}
