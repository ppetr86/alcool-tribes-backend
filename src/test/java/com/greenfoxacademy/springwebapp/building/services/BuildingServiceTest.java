package com.greenfoxacademy.springwebapp.building.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.repositories.BuildingRepository;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class BuildingServiceTest {
  private Environment env;
  private BuildingRepository buildingRepository;
  private TimeService timeService;
  private ResourceService resourceService;

  private BuildingService buildingService;
  private BuildingServiceImpl buildingServiceImpl;

  @Before
  public void init() {

    buildingRepository = Mockito.mock(BuildingRepository.class);
    timeService = Mockito.mock(TimeService.class);
    env = Mockito.mock(Environment.class);
    resourceService = Mockito.mock(ResourceService.class);

    buildingService = new BuildingServiceImpl(env, buildingRepository, timeService, resourceService);
    buildingServiceImpl = new BuildingServiceImpl(env, buildingRepository, timeService, resourceService);

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

  @Test
  public void updateResource_returnsCorrectResource() {
    KingdomEntity kingdom = new KingdomEntity();
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.MINE, 1, 100,
        10L, 1000L);

    Mockito.when(resourceService.updateResourceGeneration(kingdom, building)).thenReturn(resource);

    ResourceEntity resourceToBeUpdated = buildingServiceImpl.updateResource(building, kingdom);

    Assert.assertEquals(resource, resourceToBeUpdated);
  }
}