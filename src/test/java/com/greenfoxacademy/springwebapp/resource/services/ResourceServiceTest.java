package com.greenfoxacademy.springwebapp.resource.services;


import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceTimerTask;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Timer;

@RunWith(MockitoJUnitRunner.class) //needed for using ArgumentCaptor
public class ResourceServiceTest {
  @Captor
  ArgumentCaptor<ResourceTimerTask> resourceTimerTaskCaptor;
  @Captor
  ArgumentCaptor<Integer> delayCaptor;

  private ResourceService resourceService;
  private TimeService timeService;
  private ResourceRepository resourceRepository;
  private Environment env;

  private ResourceServiceImpl resourceServiceImpl;

  @Before
  public void setUp() {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    timeService = Mockito.mock(TimeService.class);
    env = Mockito.mock(Environment.class);
    resourceService = new ResourceServiceImpl(resourceRepository, timeService, env);

    resourceServiceImpl = new ResourceServiceImpl(resourceRepository, timeService, env);
  }

  @Test
  public void updateResourceGeneration_returnsCorrectResource() {
    resourceServiceImpl = Mockito.spy(resourceServiceImpl);
    KingdomEntity kingdom = new KingdomEntity();
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.MINE, 1, 100,
        10L, 1000L);

    Mockito.doReturn(resource).when(resourceServiceImpl).findResourceByBuildingType(kingdom, building.getType());
    Mockito.doReturn(null).when(resourceServiceImpl).doResourceUpdate(kingdom, building, resource);

    ResourceEntity resourceToBeUpdated = resourceServiceImpl.updateResourceGeneration(kingdom, building);

    Assert.assertEquals(resource, resourceToBeUpdated);
  }

  @Test
  public void updateResourceGeneration_wrongBuildingTypeReturnsNull() {
    KingdomEntity kingdom = KingdomFactory.createFullKingdom(1L, 1L);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.ACADEMY, 1, 100,
        10L, 1000L);

    ResourceEntity resourceToBeUpdated = resourceServiceImpl.updateResourceGeneration(kingdom, building);

    Assert.assertEquals(null, resourceToBeUpdated);
  }

  @Test
  public void doResourceUpdate_passesCorrectArgumentsToScheduleResourceUpdateMethod() {
    resourceServiceImpl = Mockito.spy(resourceServiceImpl);
    KingdomEntity kingdom = new KingdomEntity();
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.MINE, 1, 100, 10L, 1000L);

    Timer mockTimer = Mockito.mock(Timer.class);
    Mockito.doReturn(100).when(resourceServiceImpl).calculateNewResourceGeneration(resource, building);
    Mockito.doReturn(mockTimer).when(resourceServiceImpl).createNewTimer();

    resourceServiceImpl.doResourceUpdate(kingdom, building, resource);

    Mockito.verify(mockTimer).schedule(resourceTimerTaskCaptor.capture(), delayCaptor.capture());
    ResourceTimerTask task = resourceTimerTaskCaptor.getValue();
    Assert.assertEquals(resource, task.getResource());
    Assert.assertEquals(100, task.getGeneration().intValue());
    Assert.assertEquals(building, task.getBuilding());
  }

  @Test
  public void scheduledResourceUpdateShouldReturnCorrectlyUpdatedResource() {
    resourceServiceImpl = Mockito.spy(new ResourceServiceImpl(resourceRepository, timeService, env));
    KingdomEntity kingdom = new KingdomEntity();
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.MINE, 1, 100,
        10L, 1000L);
    int newResourceGeneration = 100;

    Mockito.when(resourceRepository.findById(1L)).thenReturn(Optional.of(resource));
    Mockito.doReturn(500).when(resourceServiceImpl).calculateResourcesUntilBuildingIsFinished(building, resource);

    ResourceEntity updatedResource = resourceServiceImpl
        .scheduledResourceUpdate(resource, newResourceGeneration, building);

    Assert.assertEquals(1L, updatedResource.getId().longValue());
    Assert.assertEquals(100, updatedResource.getGeneration().intValue());
    Assert.assertEquals(600, updatedResource.getAmount().intValue());
    Assert.assertEquals(1000L, updatedResource.getUpdatedAt().longValue());
  }

  @Test
  public void createDefaultResourcesShouldReturnCorrectResources() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    kingdomEntity.setId(1L);

    List<ResourceEntity> result = resourceService.createDefaultResources(kingdomEntity);
    Integer expectedAmount = 100;
    Integer expectedGeneration = 10;

    Assert.assertEquals("food", result.get(0).getType().resourceType);
    Assert.assertEquals(expectedAmount, result.get(0).getAmount());
    Assert.assertEquals(expectedGeneration, result.get(0).getGeneration());
    Assert.assertEquals("gold", result.get(1).getType().resourceType);
    Assert.assertEquals(expectedAmount, result.get(1).getAmount());
    Assert.assertEquals(expectedGeneration, result.get(1).getGeneration());
  }

  @Test
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange100() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    int amountChange = 100;

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange100() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, 100);

    Assert.assertFalse(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange150() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    int amountChange = 150;

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange150() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    int amountChange = 150;

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, amountChange);

    Assert.assertFalse(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange200() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    int amountChange = 200;

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange200() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    int amountChange = 200;

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(kingdom, amountChange);

    Assert.assertFalse(result);
  }

  @Test
  public void hasResourcesForTroopShouldReturnTrue() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForTroop(kingdom, 25);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForTroopShouldReturnFalse() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForTroop(kingdom, 25);

    Assert.assertFalse(result);
  }

  @Test
  public void findResourceByBuildingType_FarmReturnsFood() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));
    ResourceEntity food = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.FOOD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceByBuildingType(kingdom, BuildingType.FARM);

    Assert.assertEquals(food, resource);
  }

  @Test
  public void calculateActualResource_ShouldReturn_ProperGoldResourceAmount() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    ResourceEntity food = kingdom.getResources().get(0);
    long now = Instant.now().getEpochSecond();

    Mockito.when(timeService.getTime()).thenReturn(now);
    Mockito.when(timeService.getTimeBetween(food.getUpdatedAt(), now)).thenReturn((int) (now - food.getUpdatedAt()));

    Integer result = resourceService.calculateActualResource(kingdom, ResourceType.GOLD);

    Assert.assertEquals(Optional.of(50), Optional.ofNullable(result));
  }

  @Test
  public void calculateActualResource_ShouldReturn_ProperFoodResourceAmount() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    ResourceEntity food = kingdom.getResources().get(1);
    long now = Instant.now().getEpochSecond();

    Mockito.when(timeService.getTime()).thenReturn(now);
    Mockito.when(timeService.getTimeBetween(food.getUpdatedAt(), now)).thenReturn((int) (now - food.getUpdatedAt()));

    Integer result = resourceService.calculateActualResource(kingdom, ResourceType.FOOD);

    Assert.assertEquals(Optional.of(50), Optional.ofNullable(result));
  }

  @Test
  public void findResourceByBuildingType_MineReturnsGold() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));
    ResourceEntity gold = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.GOLD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceByBuildingType(kingdom, BuildingType.MINE);

    Assert.assertEquals(gold, resource);
  }

  @Test
  public void ffindResourceByBuildingType_AcademyReturnsNull() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createDefaultResources(kingdom));

    ResourceEntity resource = resourceService.findResourceByBuildingType(kingdom, BuildingType.ACADEMY);

    Assert.assertNull(resource);
  }

  @Test
  public void calculateNewResourceGeneration_food_returnsCorrectGenerationAmount() {
    BuildingEntity buildingLevel5 = new BuildingEntity(5L, BuildingType.FARM, 5, 200,
        1L, 2L);
    ResourceEntity foodResource = new ResourceEntity(1L, ResourceType.FOOD, 100,
        50, 1L, new KingdomEntity());
    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");

    int amount = resourceServiceImpl.calculateNewResourceGeneration(foodResource, buildingLevel5);

    Assert.assertEquals(80, amount);
  }

  @Test
  public void calculateNewResourceGeneration_gold_returnsCorrectGenerationAmount() {
    BuildingEntity buildingLevel5 = new BuildingEntity(5L, BuildingType.MINE, 5, 200,
        1L, 2L);
    ResourceEntity goldResource = new ResourceEntity(1L, ResourceType.GOLD, 100,
        50, 1L, new KingdomEntity());

    Mockito.when(env.getProperty("resourceEntity.gold")).thenReturn("10");

    int amount = resourceServiceImpl.calculateNewResourceGeneration(goldResource, buildingLevel5);

    Assert.assertEquals(110, amount);
  }

  @Test
  public void calculateResourcesUntilBuildingIsFinished_correctResult() {
    BuildingEntity building = new BuildingEntity(5L, BuildingType.MINE, 5, 200, 1L,
        500L);
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 50,
        300L, new KingdomEntity());
    Mockito.when(timeService.getTimeBetween(300L, 500L)).thenReturn(200);

    int resourcesGenerated = resourceServiceImpl.calculateResourcesUntilBuildingIsFinished(building, resource);

    Assert.assertEquals(166, resourcesGenerated);
  }
}