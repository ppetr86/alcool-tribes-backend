package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.building.services.BuildingServiceImpl;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildingServiceTest {

  private BuildingService buildingService;
  private ResourceService resourceService;
  private TimeService timeService;

  @Before
  public void init() {

    BuildingRepository buildingRepository = Mockito.mock(BuildingRepository.class);
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

    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 100, 200));
    fakeList.add(new BuildingEntity(2L, BuildingType.ACADEMY, 1, 100, 100, 200));
    fakeList.add(new BuildingEntity(3L, BuildingType.FARM, 1, 100, 100, 200));
    fakeList.add(new BuildingEntity(4L, BuildingType.MINE, 1, 100, 100, 200));

    Mockito.when(buildingRepository.findAllByKingdomId(1L)).thenReturn(fakeList);
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
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(120, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Farm() {
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Mine() {
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(60, b.getFinishedAt());
  }

  @Test
  public void defineFinishedAt_Academy() {
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    buildingService.defineFinishedAt(b);
    Assert.assertEquals(90, b.getFinishedAt());
  }

  @Test
  public void defineHP_Townhall() {
    BuildingEntity b = new BuildingEntity(BuildingType.TOWNHALL, 0L);
    buildingService.defineHp(b);
    Assert.assertEquals(200, b.getHp());
  }

  @Test
  public void defineHP_Farm() {
    BuildingEntity b = new BuildingEntity(BuildingType.FARM, 0L);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp());
  }

  @Test
  public void defineHP_Mine() {
    BuildingEntity b = new BuildingEntity(BuildingType.MINE, 0L);
    buildingService.defineHp(b);
    Assert.assertEquals(100, b.getHp());
  }

  @Test
  public void defineHP_Academy() {
    BuildingEntity b = new BuildingEntity(BuildingType.ACADEMY, 0L);
    buildingService.defineHp(b);
    Assert.assertEquals(150, b.getHp());
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

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnNoId() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(BuildingType.TOWNHALL,200L );
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(townHall);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdomEntity, null);

    Assert.assertEquals("no id", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnParameterMissingIdLevelHpFinishedAtKingdom(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(BuildingType.TOWNHALL, 3000L);
    BuildingEntity building = new BuildingEntity(BuildingType.FARM, 4000L);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("parameter missing", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnParameterMissingIdHpStatedAtFinishedAt(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(BuildingType.TOWNHALL, 3000L);
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("parameter missing", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnParameterMissingKingdom(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(BuildingType.TOWNHALL, 3000L);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("parameter missing", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnTownHallNeedHigherLevelIfLevelsAreEquals(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 1);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("town hall need higher level", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnTownHallNeedHigherLevelIfTownHallsLevelSmaller(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 1);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 2, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("town hall need higher level", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnNoResource(){
    //Arrange
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("no resource", result);
  }

  @Test
  public void increaseTheGivenBuildingLevelMethodShouldReturnBuildingDetails(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    String result = buildingService.increaseTheGivenBuildingLevel(kingdom, building);

    Assert.assertEquals("building details", result);
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedBuilding(){
    BuildingEntity building = new BuildingEntity(new KingdomEntity(), BuildingType.FARM, 1);

    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(building);

    Assert.assertEquals(2, building.getLevel());
    Assert.assertEquals(1060, building.getStartedAt());
    Assert.assertEquals(1120, building.getFinishedAt());
  }

  @Test
  public void updateBuildingShouldNotReturnWithUpdatedBuilding(){
    BuildingEntity building = new BuildingEntity(new KingdomEntity(), BuildingType.FARM, 1);

    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(building);

    Assert.assertNotEquals(3, building.getLevel());
    Assert.assertEquals(1060, building.getStartedAt());
    Assert.assertNotEquals(1060, building.getFinishedAt());
  }
}