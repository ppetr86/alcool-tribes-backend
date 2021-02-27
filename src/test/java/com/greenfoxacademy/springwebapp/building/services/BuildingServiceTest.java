package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BuildingServiceTest {

  private BuildingService buildingService;
  private BuildingRepository buildingRepository;

  @Before
  public void init() {

    buildingRepository = Mockito.mock(BuildingRepository.class);
    TimeService timeService = Mockito.mock(TimeService.class);
    Environment env = Mockito.mock(Environment.class);
    ResourceService resourceService = Mockito.mock(ResourceService.class);

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


  //Mark's tests
  @Test
  public void findBuildingByIdShouldReturnWithCorrectBuildingType() {
    BuildingEntity buildingEntity = new BuildingEntity(null, BuildingType.FARM, 0);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    String buildingType = buildingService.findBuildingById(1L).getType().toString();

    Assert.assertEquals("FARM", buildingType);
  }

  @Test
  public void findBuildingByIdShouldReturnWithUnCorrectBuildingType() {
    BuildingEntity buildingEntity = new BuildingEntity(null, BuildingType.MINE, 0);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    String buildingType = buildingService.findBuildingById(1L).getType().toString();

    Assert.assertNotEquals("FARM", buildingType);
  }

  @Test
  public void showBuildingShouldReturnCorrectBuildingDetails(){
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(1L)).thenReturn(Optional.of(buildingEntity));

    BuildingDetailsDTO result = buildingService.showBuilding(kingdomEntity, 1L);

    Assert.assertEquals(1, result.getId());
    Assert.assertEquals(1, result.getLevel());
    Assert.assertEquals("farm", result.getType());
    Assert.assertEquals(100, result.getHp());
  }

  @Test
  public void showBuildingShouldNotReturnWithBuildingDetails(){
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(2L)).thenReturn(Optional.of(buildingEntity));

    BuildingDetailsDTO result = buildingService.showBuilding(kingdomEntity, 2L);

    Assert.assertNotEquals(1, result.getId());
    Assert.assertNotEquals(1, result.getLevel());
    Assert.assertNotEquals("farm", result.getType());
    Assert.assertNotEquals(100, result.getHp());
  }

  @Test(expected = IdNotFoundException.class)
  public void showBuildingShouldReturnWithIdNotFoundException(){
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity buildingEntity = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(3L)).thenReturn(Optional.empty());

    BuildingDetailsDTO response = buildingService.showBuilding(kingdomEntity, 3L);
  }

  @Test(expected = ForbiddenActionException.class)
  public void showBuildingShouldReturnWithForbiddenException(){
    BuildingEntity fakeBuilding2 = new BuildingEntity(3L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    KingdomEntity kingdomEntity = new KingdomEntity();
    BuildingEntity fakeBuilding = new BuildingEntity(2L, BuildingType.MINE, 2, 200, 100L, 200L, null);
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(fakeBuilding);
    kingdomEntity.setBuildings(fakeList);

    Mockito.when(buildingRepository.findById(3L)).thenReturn(Optional.of(fakeBuilding2));

    BuildingDetailsDTO response = buildingService.showBuilding(kingdomEntity, 3L);
  }
}
