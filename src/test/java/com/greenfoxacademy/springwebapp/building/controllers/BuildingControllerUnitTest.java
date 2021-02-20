package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.*;
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
import java.util.Arrays;
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
  public void increaseTheGivenBuildingLevelShouldReturnNoId() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(3L)).thenReturn(null);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, null)).thenReturn(String.valueOf(new IdNotFoundException()));

    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication, level);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnParameterMissing() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L)).thenReturn(String.valueOf(new MissingParameterException("type")));
    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);

    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication, level);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Missing parameter(s): type!", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnTownHallNeedHigherLevel() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 1);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L)).thenReturn(String.valueOf(new TownhallLevelException()));
    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);

    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication, level);

    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    Assert.assertEquals("Cannot build buildings with higher level than the Townhall", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnNoResource() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L)).thenReturn(String.valueOf(new NotEnoughResourceException()));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication, level);

    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    Assert.assertEquals("Not enough resource", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnOkWithUpdatedBuildings() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L)).thenReturn("building details");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    building.setLevel(2);
    building.setStartedAt(100L);
    building.setFinishedAt(160L);
    Mockito.when(buildingService.updateBuilding(1L)).thenReturn(building);

    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication, level);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    // Assert.assertEquals(2, ((BuildingEntity)response.getBody()).getLevel());
    // Assert.assertEquals(100, ((BuildingEntity)response.getBody()).getStartedAt());
    // Assert.assertEquals(160, ((BuildingEntity)response.getBody()).getFinishedAt());
  }
}
