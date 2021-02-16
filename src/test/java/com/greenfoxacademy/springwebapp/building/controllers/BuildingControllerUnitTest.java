package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static org.mockito.ArgumentMatchers.any;

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
    buildingController = new BuildingController(buildingService);
  }

  @Test
  public void getKingdomBuildings_ReturnsCorrectStatusCode() {
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 1, 2));
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
  public void increaseTheGivenBuildingLevelShouldReturnNoId(){
    //Arrange
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(3L)).thenReturn(null);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, null)).thenReturn("no id");
    //Act
    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication);
    //Assert

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnParameterMissing(){
    //Arrange
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("parameter missing");
    //Act
    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication);
    //Assert

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Missing parameter(s): <type>!", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnTownHallNeedHigherLevel(){
    //Arrange
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 1);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("town hall need higher level");
    //Act
    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication);
    //Assert

    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    Assert.assertEquals("Invalid building level || Cannot build buildings with higher level than the Townhall", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnNoResource(){
    //Arrange
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("no resource");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    //Act
    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication);
    //Assert

    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    Assert.assertEquals("Not enough resource", ((ErrorDTO)response.getBody()).getMessage());
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturnOkWithUpdatedBuildings(){
    //Arrange
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("building details");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    building.setLevel(2);
    building.setStartedAt(100L);
    building.setFinishedAt(160L);
    Mockito.when(buildingService.updateBuilding(building)).thenReturn(building);
    //Act
    ResponseEntity<?> response = buildingController.increaseTheGivenBuildingLevel(1L, authentication);

    //Assert

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals(2, ((BuildingEntity)response.getBody()).getLevel());
    Assert.assertEquals(100, ((BuildingEntity)response.getBody()).getStartedAt());
    Assert.assertEquals(160, ((BuildingEntity)response.getBody()).getFinishedAt());
  }
}
