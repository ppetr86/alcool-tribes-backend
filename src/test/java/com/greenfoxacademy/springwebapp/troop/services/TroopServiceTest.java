package com.greenfoxacademy.springwebapp.troop.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;


import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TroopServiceTest {

  private TroopService troopService;
  private ResourceService resourceService;
  private TimeService timeService;
  private TroopRepository troopRepository;
  private Environment env;
  private BuildingService buildingService;

  @Before
  public void init() {
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    troopRepository = Mockito.mock(TroopRepository.class);
    env = Mockito.mock(Environment.class);
    buildingService = Mockito.mock(BuildingService.class);
    troopService = new TroopServiceImpl(resourceService,timeService,troopRepository,env, buildingService);
  }

  @Test
  public void troopsToListDTO_ReturnsCorrectResult() {
    KingdomEntity ke = new KingdomEntity();
    ke.setTroops(TroopFactory.createDefaultTroops());

    TroopListResponseDto result = troopService.troopsToListDTO(ke);

    Assert.assertEquals(3, result.getTroops().size());
    Assert.assertEquals(101, (long) result.getTroops().get(0).getFinishedAt());
  }

  @Test(expected = ForbiddenActionException.class)
  public void createTroopThrowsForbiddenCustomException_BuildingIdExistsButDoesNotBelongToMyKingdom() {
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(2L, BuildingType.ACADEMY, 1, 1, 1L, 1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom, requestDTO);
  }

  @Test(expected = InvalidAcademyIdException.class)
  public void createTroopThrowsInvalidAcademyIdException_BuildingExistsButItIsNotAcademy() {
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 1, 1L, 1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom, requestDTO);
  }

  @Test(expected = NotEnoughResourceException.class)
  public void createTroopThrowsNotEnoughResourceException() {
    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.ACADEMY, 1, 1, 1L, 1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(false);
    // TODO: after resources are defined, this method will be updated, so test should be updated as well

    TroopEntityResponseDTO response = troopService.createTroop(kingdom, requestDTO);
  }

  @Test
  public void createTroopReturnsLevel1CreatedTroopAsDTO() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L,1L); //academy is id2
    TroopEntity fakeTroop = new TroopEntity(1L,1, 20, 10, 5, 1L, 30L, kingdom);
    TroopRequestDTO requestDTO = new TroopRequestDTO(2L);
    TroopEntityResponseDTO expectedTroop = new TroopEntityResponseDTO(1L,1,20,10,5,1,30);

    Mockito.when(env.getProperty("troop.hp")).thenReturn("20");
    Mockito.when(env.getProperty("troop.food")).thenReturn("-5");
    Mockito.when(env.getProperty("troop.attack")).thenReturn("10");
    Mockito.when(env.getProperty("troop.defence")).thenReturn("5");
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");
    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(true);
    Mockito.when(timeService.getTime()).thenReturn(1L);
    Mockito.when(timeService.getTimeAfter(1
        * Integer.parseInt(env.getProperty("troop.buildingTime")))).thenReturn(30L);
    Mockito.when(troopRepository.save(any())).thenReturn(fakeTroop);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom, requestDTO);
    assertThat(response).isEqualToComparingFieldByField(expectedTroop);
  }

  @Test(expected = ForbiddenActionException.class)
  public void getTroopThrowsForbiddenActionException_TroopIdExistsButDoesNotBelongToMyKingdom() {
    TroopEntity fakeTroop = new TroopEntity(1L, 10, 20, 30, 40, 1L, 2L);
    TroopEntity fakeTroop2 = new TroopEntity(10L, 100, 200, 300, 400, 10L, 20L);
    KingdomEntity kingdom = new KingdomEntity();
    List<TroopEntity> troops = new ArrayList<>();
    troops.add(fakeTroop);
    kingdom.setTroops(troops);

    Mockito.when(troopRepository.findById(10L)).thenReturn(Optional.of(fakeTroop2));

    TroopEntityResponseDTO response = troopService.getTroop(kingdom, 10L);
  }

  @Test(expected = IdNotFoundException.class)
  public void getTroopThrowsIdNotFoundException_TroopIdDoesNotExistsInDatabase() {
    TroopEntity fakeTroop = new TroopEntity(1L, 10, 20, 30, 40, 1L, 2L);
    KingdomEntity kingdom = new KingdomEntity();
    List<TroopEntity> troops = new ArrayList<>();
    troops.add(fakeTroop);
    kingdom.setTroops(troops);

    Mockito.when(troopRepository.findById(10L)).thenReturn(Optional.ofNullable(null));

    TroopEntityResponseDTO response = troopService.getTroop(kingdom, 10L);
  }

  @Test
  public void getTroopReturnsCorrectTroopEntityResponseDTO() {
    TroopEntity fakeTroop = new TroopEntity(1L, 10, 20, 30, 40, 1L, 2L);

    KingdomEntity kingdom = new KingdomEntity();
    List<TroopEntity> troops = new ArrayList<>();
    troops.add(fakeTroop);
    kingdom.setTroops(troops);

    Mockito.when(troopRepository.findById(1L)).thenReturn(Optional.of(fakeTroop));

    TroopEntityResponseDTO response = troopService.getTroop(kingdom, 1L);

    Assert.assertEquals(1L, response.getId().longValue());
    Assert.assertEquals(10, response.getLevel());
    Assert.assertEquals(40, response.getDefence());
  }

  @Test
  public void updateTroopLevelShouldReturnCorrectUpdatedValues() {
    KingdomEntity fakeKingdom = KingdomFactory.createFullKingdom(1L, 1L);
    TroopRequestDTO fakeTroopRequest = new TroopRequestDTO(2L);
    fakeKingdom.getBuildings().get(1).setLevel(7);

    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(true);
    Mockito.when(troopRepository.findKingdomIdByTroopId(fakeKingdom.getTroops().get(0).getId()))
        .thenReturn(fakeKingdom.getId());
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");

    TroopEntityResponseDTO response = troopService.updateTroopLevel(fakeKingdom, fakeTroopRequest, 1L);

    Assert.assertEquals(1L, response.getId().longValue());
    Assert.assertEquals(100L, response.getHp());
    Assert.assertEquals(7L, response.getLevel());
    Assert.assertEquals(timeService.getTime(), response.getStartedAt());
    Assert.assertEquals(timeService.getTimeAfter(30), response.getFinishedAt());
  }

  @Test(expected = NotEnoughResourceException.class)
  public void updateTroopLevelShouldReturnNotEnoughResourcesException() {
    List<TroopEntity> fakeTroopList = TroopFactory.createDefaultTroops();
    KingdomEntity fakeKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    fakeTroopList.get(0).setKingdom(fakeKingdom);
    List<BuildingEntity> fakeBuildingList = BuildingFactory.createBuildings(fakeKingdom);
    fakeKingdom.setBuildings(fakeBuildingList);
    TroopRequestDTO fakeTroopRequest = new TroopRequestDTO(2L);

    Mockito.when(buildingService.findBuildingById(2L)).thenReturn(fakeBuildingList.get(1));
    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(false);
    Mockito.when(troopRepository.findKingdomIdByTroopId(fakeTroopList.get(0).getId())).thenReturn(fakeKingdom.getId());
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");

    TroopEntityResponseDTO response = troopService.updateTroopLevel(fakeKingdom, fakeTroopRequest, 1L);
  }

  @Test(expected = MissingParameterException.class)
  public void updateTroopLevelShouldReturnMissingParameterException() {
    List<TroopEntity> fakeTroopList = TroopFactory.createDefaultTroops();
    KingdomEntity fakeKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    fakeTroopList.get(0).setKingdom(fakeKingdom);
    List<BuildingEntity> fakeBuildingList = BuildingFactory.createBuildings(fakeKingdom);
    fakeKingdom.setBuildings(fakeBuildingList);
    TroopRequestDTO fakeTroopRequest = new TroopRequestDTO();

    Mockito.when(buildingService.findBuildingById(2L)).thenReturn(fakeBuildingList.get(1));
    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(true);
    Mockito.when(troopRepository.findKingdomIdByTroopId(fakeTroopList.get(0).getId())).thenReturn(fakeKingdom.getId());
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");

    TroopEntityResponseDTO response = troopService.updateTroopLevel(fakeKingdom, fakeTroopRequest, 1L);
  }

  @Test(expected = IdNotFoundException.class)
  public void updateTroopLevelShouldReturnIdNotFoundException() {
    List<TroopEntity> fakeTroopList = TroopFactory.createDefaultTroops();
    KingdomEntity fakeKingdom = KingdomFactory.createKingdomEntityWithId(1L);
    fakeTroopList.get(0).setKingdom(fakeKingdom);
    List<BuildingEntity> fakeBuildingList = BuildingFactory.createBuildings(fakeKingdom);
    fakeKingdom.setBuildings(fakeBuildingList);
    TroopRequestDTO fakeTroopRequest = new TroopRequestDTO(5L);

    Mockito.when(buildingService.findBuildingById(5L)).thenReturn(null);
    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(true);
    Mockito.when(troopRepository.findKingdomIdByTroopId(fakeTroopList.get(0).getId())).thenReturn(fakeKingdom.getId());
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");

    TroopEntityResponseDTO response = troopService.updateTroopLevel(fakeKingdom, fakeTroopRequest, 1L);
  }
}