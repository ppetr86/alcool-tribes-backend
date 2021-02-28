package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.Optional;

public class BuildingServiceTest {

  private BuildingService buildingService;
  private ResourceService resourceService;
  private TimeService timeService;
  private BuildingRepository buildingRepository;

  @Before
  public void init() {

    buildingRepository = Mockito.mock(BuildingRepository.class);
    timeService = Mockito.mock(TimeService.class);
    Environment env = Mockito.mock(Environment.class);
    resourceService = Mockito.mock(ResourceService.class);

    buildingService = new BuildingServiceImpl(env, buildingRepository, timeService, resourceService);

    Mockito.when(env.getProperty("building.townhall.buildingTime"))
        .thenReturn("120");
    Mockito.when(env.getProperty("building.farm.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.mine.buildingTime"))
        .thenReturn("60");
    Mockito.when(env.getProperty("building.academy.buildingTime"))
        .thenReturn("90");

    Mockito.when(env.getProperty("building.townhall.hp"))
        .thenReturn("200");
    Mockito.when(env.getProperty("building.farm.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.mine.hp"))
        .thenReturn("100");
    Mockito.when(env.getProperty("building.academy.hp"))
        .thenReturn("150");
    Mockito.when(buildingRepository.findAllByKingdomId(1L)).thenReturn(BuildingFactory.createBuildings(null));
  }

  @Test
  public void findBuildingsByKingdomId_correct() {
    Assert.assertEquals(4, buildingService.findBuildingsByKingdomId(1L).size());
  }

  @Test
  public void findBuildingsByKingdomId_wrong() {
    Assert.assertNotEquals(999, buildingService.findBuildingsByKingdomId(1L).size());
  }

  @Test
  public void defineFinishedAt_Townhall() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(0);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(120, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Farm() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(2);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Mine() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(3);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineFinishedAt_Academy() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(1);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(90, b.getFinishedAt() - b.getStartedAt());
  }

  @Test
  public void defineHP_Townhall() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(0);
    buildingService.defineHp(b);
    Assert.assertEquals(200, b.getHp().longValue());
  }

  @Test
  public void defineHP_Farm() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(2);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp().longValue());
  }

  @Test
  public void defineHP_Mine() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(3);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp().longValue());
  }

  @Test
  public void defineHP_Academy() {
    BuildingEntity b = BuildingFactory.createBuildings(null).get(1);
    buildingService.defineHp(b);
    Assert.assertEquals(150, b.getHp().longValue());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_VariousCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("ToWNhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_LowerCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("townhall").getType());
  }

  @Test
  public void isTypeOkRequest_Townhall_ShouldTrue_UpperCase() {
    Assert.assertEquals(BuildingType.TOWNHALL, buildingService.setBuildingTypeOnEntity("TOWNHALL").getType());
  }

  @Test(expected = NullPointerException.class)
  public void isTypeOkRequest_Townhall_ShouldNull_UpperCase() {
    Assert.assertNull(buildingService.setBuildingTypeOnEntity("TOWNHALLL").getType());
  }

  @Test
  public void isTypeOkRequest_Farm_ShouldTrue_UpperCase() {
    Assert.assertEquals(BuildingType.FARM, buildingService.setBuildingTypeOnEntity("FARM").getType());
  }

  @Test
  public void isTypeOkRequest_Mine_ShouldTrue_LowerCase() {
    Assert.assertEquals(BuildingType.MINE,
        buildingService.setBuildingTypeOnEntity("mine").getType());
  }

  @Test
  public void isTypeOkRequest_Academy_ShouldTrue_VariousCase() {
    Assert.assertEquals(BuildingType.ACADEMY,
      buildingService.setBuildingTypeOnEntity("ACAdemy").getType());
  }

  @Test(expected = IdNotFoundException.class)
  public void updateBuildingShouldReturnIdNotFoundException(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2))).thenThrow(IdNotFoundException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2));
  }

  @Test(expected = MissingParameterException.class)
  public void updateBuildingShouldReturnMissingParameterException(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO())).thenThrow(MissingParameterException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO());
  }

  @Test(expected = ForbiddenActionException.class)
  public void updateBuildingShouldReturnForbiddenActionException(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    KingdomEntity kingdom2 = new KingdomEntity();
    kingdom2.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2))).thenThrow(ForbiddenActionException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(2));
  }

  @Test(expected = NotEnoughResourceException.class)
  public void updateBuildingShouldReturnNotEnoughResourceException(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    Mockito.when(buildingService.updateBuilding(kingdom, 1L, new BuildingLevelDTO(2))).thenThrow(NotEnoughResourceException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 1L, new BuildingLevelDTO(2));
  }

  @Test(expected = TownhallLevelException.class)
  public void updateBuildingShouldReturnTownhallLevelException(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8());

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    Mockito.when(buildingService.updateBuilding(kingdom, 2L, new BuildingLevelDTO(2))).thenThrow(TownhallLevelException.class);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 2L, new BuildingLevelDTO(2));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedTownHall(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereBuildingsIdAre_5_8());
    BuildingEntity townHall = kingdom.getBuildings().get(0);

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(1L);
    Mockito.when(buildingRepository.findById(5L)).thenReturn(Optional.of(townHall));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 5L, new BuildingLevelDTO(4));

    Assert.assertEquals(Optional.of(4), Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(Optional.of(800), Optional.ofNullable(result.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(Optional.of(1540L), Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedAcademy(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    BuildingEntity academy = kingdom.getBuildings().get(1);
    academy.setLevel(3);
    academy.setHp(450);
    academy.setStartedAt(1060L);
    academy.setFinishedAt(1330L);

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingRepository.findById(2L)).thenReturn(java.util.Optional.of(academy));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom, 2L, new BuildingLevelDTO(3));

    Assert.assertEquals(java.util.Optional.of(3), java.util.Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(java.util.Optional.of(450), java.util.Optional.ofNullable(result.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1330L), java.util.Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedFarm(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    BuildingEntity farm = kingdom.getBuildings().get(2);

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingRepository.findById(3L)).thenReturn(Optional.of(farm));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    BuildingEntity result = buildingService.updateBuilding(kingdom,3L, new BuildingLevelDTO(2));

    Assert.assertEquals(Optional.of(2), Optional.ofNullable(result.getLevel()));
    Assert.assertEquals(Optional.of(200), Optional.ofNullable(result.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(result.getStartedAt()));
    Assert.assertEquals(Optional.of(1180L), Optional.ofNullable(result.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedMine(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    BuildingEntity mine = kingdom.getBuildings().get(3);

    buildingService = Mockito.spy(BuildingService.class);
    Mockito.doReturn(kingdom.getBuildings()).when(buildingService).findBuildingsByKingdomId(kingdom.getId());
    Mockito.when(buildingRepository.findById(4L)).thenReturn(Optional.of(mine));
    Mockito.when(timeService.getTime()).thenReturn(1060L);
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    buildingService.updateBuilding(kingdom, 4L, new BuildingLevelDTO(4));

    Assert.assertEquals(Optional.of(4), Optional.ofNullable(mine.getLevel()));
    Assert.assertEquals(Optional.of(400), Optional.ofNullable(mine.getHp()));
    Assert.assertEquals(Optional.of(1060L), Optional.ofNullable(mine.getStartedAt()));
    Assert.assertEquals(Optional.of(1300L), Optional.ofNullable(mine.getFinishedAt()));
  }
}