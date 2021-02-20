package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenCustomException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class TroopServiceTest {

  private TroopService troopService;
  private ResourceService resourceService;
  private TimeService timeService;
  private TroopRepository troopRepository;
  private Environment env;

  @Before
  public void init() {
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    troopRepository = Mockito.mock(TroopRepository.class);
    env = Mockito.mock(Environment.class);
    troopService = new TroopServiceImpl(resourceService,timeService,troopRepository,env);
  }

  @Test
  public void troopsToListDTO_ReturnsCorrectResult() {
    KingdomEntity ke = new KingdomEntity();
    ke.setTroops(TroopFactory.createDefaultTroops());

    TroopListResponseDto result = troopService.troopsToListDTO(ke);

    Assert.assertEquals(3, result.getTroops().size());
    Assert.assertEquals(101, (long) result.getTroops().get(0).getFinishedAt());
  }

  @Test(expected = ForbiddenCustomException.class)
  public void createTroopThrowsForbiddenCustomException() {
    //preparing requestDTO
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    //preparing kingdom with 1 building (e.g., academy, it could be anything),
    // but  building has different id then requested building
    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(2L, BuildingType.ACADEMY,1,1,1L,1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom,requestDTO);
  }

  @Test(expected = InvalidAcademyIdException.class)
  public void createTroopThrowsInvalidAcademyIdException() {
    //preparing requestDTO
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    //preparing kingdom with building matching requested id, but it is not ACADEMY
    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.TOWNHALL,1,1,1L,1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom,requestDTO);
  }

  @Test(expected = NotEnoughResourceException.class)
  public void createTroopThrowsNotEnoughResourceException() {
    //preparing requestDTO
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    //preparing kingdom with matching ACADEMY
    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.ACADEMY,1,1,1L,1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(false);
    // TODO: after resources are defined, this method will be updated, so test should be updated as well

    TroopEntityResponseDTO response = troopService.createTroop(kingdom,requestDTO);
  }

  @Test
  public void createTroopReturnsLevel1CreatedTroopAsDTO() {
    //preparing requestDTO
    TroopRequestDTO requestDTO = new TroopRequestDTO(1L);

    //preparing kingdom with matching ACADEMY
    KingdomEntity kingdom = new KingdomEntity();
    List<BuildingEntity> buildings = new ArrayList<>();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.ACADEMY,1,1,1L,1L);
    buildings.add(building);
    kingdom.setBuildings(buildings);

    //preparing fake troop
    TroopEntity fakeTroop = new TroopEntity(1, 20, 10, 5, 1L, 30L, kingdom);

    Mockito.when(env.getProperty("troop.hp")).thenReturn("20");
    Mockito.when(env.getProperty("troop.food")).thenReturn("-5");
    Mockito.when(env.getProperty("troop.attack")).thenReturn("10");
    Mockito.when(env.getProperty("troop.defence")).thenReturn("5");
    Mockito.when(env.getProperty("troop.buildingTime")).thenReturn("30");
    Mockito.when(resourceService.hasResourcesForTroop()).thenReturn(true);
    Mockito.when(timeService.getTime()).thenReturn(1L);
    Mockito.when(timeService.getTimeAfter(building.getLevel()
        * Integer.parseInt(env.getProperty("troop.buildingTime")))) //using application.properties
        .thenReturn(30L);
    Mockito.when(troopRepository.save(fakeTroop)).thenReturn(fakeTroop);

    TroopEntityResponseDTO response = troopService.createTroop(kingdom,requestDTO);
    Assert.assertEquals(1, response.getLevel());
    Assert.assertEquals(20, response.getHp());
    Assert.assertEquals(10, response.getAttack());
    Assert.assertEquals(5, response.getDefence());
    Assert.assertEquals(1, response.getStartedAt());
    Assert.assertEquals(30, response.getFinishedAt());

  }
}