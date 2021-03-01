package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
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

public class BuildingControllerTest {

  private BuildingController buildingController;
  private BuildingService buildingService;
  private ResourceService resourceService;
  private Authentication authentication;

  @Before
  public void setUp() {
    authentication = createAuth("test", 1L);

    buildingService = Mockito.mock(BuildingService.class);
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

  @Test(expected = IdNotFoundException.class)
  public void updateTheGivenBuildingDetailsShouldReturnNoId() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);

    Mockito.when(buildingService.updateBuilding(kingdom, 3L, level)).thenThrow(IdNotFoundException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(3L, authentication, level);
  }

  @Test(expected = MissingParameterException.class)
  public void updateTheGivenBuildingDetailsShouldReturnParameterMissing() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, level)).thenThrow(MissingParameterException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);
  }

  @Test(expected = TownhallLevelException.class)
  public void updateTheGivenBuildingDetailsShouldReturnTownHallNeedHigherLevel() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, level)).thenThrow(TownhallLevelException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);
  }

  @Test(expected = NotEnoughResourceException.class)
  public void updateTheGivenBuildingDetailsShouldReturnNoResource() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO level = new BuildingLevelDTO(3);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, level)).thenThrow(NotEnoughResourceException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, level);
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnOkWithUpdatedBuildings() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    BuildingEntity building = kingdom.getBuildings().get(2);
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(3);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, levelDTO))
        .thenReturn(building);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
