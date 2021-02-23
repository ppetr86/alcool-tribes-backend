package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
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

import java.util.Arrays;
import java.util.List;

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
    Assert.assertEquals(100,  b.getHp().longValue());
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
  public void checkBuildingDetailsShouldReturnIdNotFoundException(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(4L)).thenReturn(java.util.Optional.empty());
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    String response = buildingService.checkBuildingDetails(kingdom, 4L, new BuildingLevelDTO(3));
  }

  @Test(expected = MissingParameterException.class)
  public void checkBuildingDetailsShouldReturnMissingHpAndFinishedAtParametersException(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 10, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));

    String response = buildingService.checkBuildingDetails(kingdom, 1L, null);

    Assert.assertEquals("Missing parameter(s): level!", response);
  }

  @Test(expected = NotEnoughResourceException.class)
  public void checkBuildingDetailsShouldReturnNowEnoughResourceException(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    String response = buildingService.checkBuildingDetails(kingdom, 1L, new BuildingLevelDTO(2));
  }

  @Test
  public void checkBuildingDetailsShouldReturnOkAndTownHall(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(1L, BuildingType.TOWNHALL, 5, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(townHall));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    String result = buildingService.checkBuildingDetails(kingdom, 1L, new BuildingLevelDTO(10));

    Assert.assertEquals("townhall", result);
  }

  @Test(expected = TownhallLevelException.class)
  public void checkBuildingDetailsShouldReturnTownHallException(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    String response = buildingService.checkBuildingDetails(kingdom, 1L, new BuildingLevelDTO(3));
  }

  @Test
  public void checkBuildingDetailsShouldReturnOkAndBuildingDetails(){
    KingdomEntity kingdom = new KingdomEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, kingdom);
    List<BuildingEntity> fakeList = Arrays.asList(
      townHall,
      building
    );
    kingdom.setBuildings(fakeList);

    //TODO: have to modify hasResourcesForBuilding method
    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    String result = buildingService.checkBuildingDetails(kingdom, 1L, new BuildingLevelDTO(2));

    Assert.assertEquals("building details", result);
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedBuildingTownHall(){

    BuildingEntity building = new BuildingEntity(1L, BuildingType.TOWNHALL, 1, 100, 3000L, 4000L, null);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(1L, new BuildingLevelDTO(4));

    Assert.assertEquals(java.util.Optional.of(4), java.util.Optional.ofNullable(building.getLevel()));
    Assert.assertEquals(java.util.Optional.of(800), java.util.Optional.ofNullable(building.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(building.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1540L), java.util.Optional.ofNullable(building.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedBuildingAcademy(){

    BuildingEntity building = new BuildingEntity(1L, BuildingType.ACADEMY, 1, 100, 3000L, 4000L, null);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(1L, new BuildingLevelDTO(3));

    Assert.assertEquals(java.util.Optional.of(3), java.util.Optional.ofNullable(building.getLevel()));
    Assert.assertEquals(java.util.Optional.of(450), java.util.Optional.ofNullable(building.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(building.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1330L), java.util.Optional.ofNullable(building.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedBuildingFarm(){

    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 3000L, 4000L, null);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(1L, new BuildingLevelDTO(2));

    Assert.assertEquals(java.util.Optional.of(2), java.util.Optional.ofNullable(building.getLevel()));
    Assert.assertEquals(java.util.Optional.of(200), java.util.Optional.ofNullable(building.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(building.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1180L), java.util.Optional.ofNullable(building.getFinishedAt()));
  }

  @Test
  public void updateBuildingShouldReturnWithUpdatedBuildingMine(){

    BuildingEntity building = new BuildingEntity(1L, BuildingType.MINE, 1, 100, 3000L, 4000L, null);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(java.util.Optional.of(building));
    Mockito.when(timeService.getTime()).thenReturn(1060L);

    buildingService.updateBuilding(1L, new BuildingLevelDTO(15));

    Assert.assertEquals(java.util.Optional.of(15), java.util.Optional.ofNullable(building.getLevel()));
    Assert.assertEquals(java.util.Optional.of(1500), java.util.Optional.ofNullable(building.getHp()));
    Assert.assertEquals(java.util.Optional.of(1060L), java.util.Optional.ofNullable(building.getStartedAt()));
    Assert.assertEquals(java.util.Optional.of(1960L), java.util.Optional.ofNullable(building.getFinishedAt()));
  }
}