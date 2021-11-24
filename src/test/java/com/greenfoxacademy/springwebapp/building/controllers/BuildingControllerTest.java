package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingDao;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
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
  private BuildingDao buildingDao;
  private ResourceService resourceService;
  private Authentication authentication;

  @Before
  public void setUp() {
    authentication = createAuth("test", 1L);

    buildingService = Mockito.mock(BuildingService.class);
    resourceService = Mockito.mock(ResourceService.class);
    buildingDao = Mockito.mock(BuildingDao.class);
    buildingController = new BuildingController(buildingService, buildingDao);
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
    KingdomEntity kingdom = new KingdomEntity();

    Mockito.when(buildingService.isBuildingTypeInRequestOk(request)).thenReturn(true);
    Mockito.when(resourceService.hasResourcesForBuilding(kingdom, 100)).thenReturn(true);

    ResponseEntity<?> response = buildingController.buildBuilding(createAuth("test", 1L), request);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void getBuildingByIdShouldReturnCorrectBuildingDetails() {
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 100L, 200L, null);
    BuildingDetailsDTO buildingDetailsDTO = new BuildingDetailsDTO();
    buildingDetailsDTO.setType(buildingEntity.getType().toString().toLowerCase());
    buildingDetailsDTO.setLevel(buildingEntity.getLevel());
    buildingDetailsDTO.setHp(buildingEntity.getHp());
    KingdomEntity kingdomEntity = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();

    Mockito.when(buildingService.showBuilding(kingdomEntity, 1L)).thenReturn(buildingDetailsDTO);

    ResponseEntity<?> response = buildingController.getBuildingById(1L, authentication);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assert.assertEquals("farm", ((BuildingDetailsDTO) response.getBody()).getType());
    Assert.assertEquals(1, ((BuildingDetailsDTO) response.getBody()).getLevel());
    Assert.assertEquals(100, ((BuildingDetailsDTO) response.getBody()).getHp());
  }

  @Test
  public void updateTheGivenBuildingDetails_ShouldReturn_UpdatedBuilding() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(2);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, levelDTO)).thenReturn(kingdom.getBuildings().get(0));

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test(expected = IdNotFoundException.class)
  public void updateTheGivenBuildingDetails_ShouldThrow_IdNotFoundException() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(2);

    Mockito.when(buildingService.updateBuilding(kingdom, 8L, levelDTO)).thenThrow(IdNotFoundException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(8L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test(expected = MissingParameterException.class)
  public void updateTheGivenBuildingDetails_ShouldThrow_MissingParameterException() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingLevelDTO levelDTO = new BuildingLevelDTO();

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, levelDTO)).thenThrow(MissingParameterException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test(expected = NotEnoughResourceException.class)
  public void updateTheGivenBuildingDetails_ShouldThrow_NotEnoughResourceException() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(10);

    Mockito.when(buildingService.updateBuilding(kingdom, 1L, levelDTO)).thenThrow(NotEnoughResourceException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(1L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test(expected = TownhallLevelException.class)
  public void updateTheGivenBuildingDetails_ShouldThrow_TownHallLevelException() {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    BuildingLevelDTO levelDTO = new BuildingLevelDTO(6);

    Mockito.when(buildingService.updateBuilding(kingdom, 2L, levelDTO)).thenThrow(TownhallLevelException.class);

    ResponseEntity<?> response = buildingController.updateTheGivenBuildingDetails(2L, authentication, levelDTO);

    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
  }
}


