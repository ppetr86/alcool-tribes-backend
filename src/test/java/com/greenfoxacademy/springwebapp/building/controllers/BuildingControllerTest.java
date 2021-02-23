package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
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

public class BuildingControllerTest {

  private BuildingController buildingController;
  private BuildingService buildingService;
  private BuildingRepository buildingRepository;
  private KingdomService kingdomService;
  private ResourceService resourceService;
  private Authentication authentication;

  @Before
  public void setUp() {
    authentication = createAuth("test", 1L);

    buildingService = Mockito.mock(BuildingService.class);
    buildingRepository = Mockito.mock(BuildingRepository.class);
    kingdomService = Mockito.mock(KingdomService.class);
    resourceService = Mockito.mock(ResourceService.class);
    buildingController = new BuildingController(buildingService);
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
  public void updateTheGivenBuildingDetailsShouldReturnNoId() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, null);
    BuildingEntity townHall = new BuildingEntity(2L, BuildingType.TOWNHALL, 2, 200, 3000L, 4000L, null);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(3L)).thenReturn(null);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 3L, 3)).thenReturn(String.valueOf(new IdNotFoundException()));

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(3L, authentication, level);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnParameterMissing() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 0, 3000L, 4000L, null);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L, 3)).thenReturn(String.valueOf(new MissingParameterException("/hp")));
    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Missing parameter(s): /hp!", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnTownHallNeedHigherLevel() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(null, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(null, BuildingType.TOWNHALL, 1);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L, 3)).thenReturn(String.valueOf(new TownhallLevelException()));
    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);

    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    Assert.assertEquals("Cannot build buildings with higher level than the Townhall", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnNoResource() {
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
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L, 3)).thenReturn(String.valueOf(new NotEnoughResourceException()));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);

    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    Assert.assertEquals("Not enough resource", ((ErrorDTO) response.getBody()).getMessage());
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnOkWithUpdatedBuildings() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, null);
    BuildingEntity townHall = new BuildingEntity(2L, BuildingType.TOWNHALL, 3, 200, 3000L, 4000L, null);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.checkBuildingDetails(kingdom, 1L, 3)).thenReturn("building details");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    building.setLevel(3);
    building.setStartedAt(100L);
    building.setFinishedAt(280L);
    Mockito.when(buildingService.updateBuilding(1L, 3)).thenReturn(building);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(java.util.Optional.of(3), java.util.Optional.ofNullable(((BuildingEntity) response.getBody()).getLevel()));
    Assert.assertEquals(java.util.Optional.of(100L), java.util.Optional.ofNullable(((BuildingEntity) response.getBody()).getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(280L), java.util.Optional.ofNullable(((BuildingEntity) response.getBody()).getFinishedAt()));
  }
}
