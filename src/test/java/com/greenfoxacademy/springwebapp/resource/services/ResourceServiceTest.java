package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Optional;
import java.util.Timer;

import static org.mockito.ArgumentMatchers.any;

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

  //resourceServiceImpl is for testing public methods which are not in interface
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
  public void findResourceBasedOnBuildingType_FarmReturnsFood() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    ResourceEntity food = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.FOOD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.FARM);

    Assert.assertEquals(food, resource);
  }

  @Test
  public void findResourceBasedOnBuildingType_MineReturnsGold() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    ResourceEntity gold = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.GOLD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.MINE);

    Assert.assertEquals(gold, resource);
  }

  @Test
  public void findResourceBasedOnBuildingType_AcademyReturnsNull() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.ACADEMY);

    Assert.assertNull(resource);
  }

  @Test
  public void calculateNewResourceGeneration_food_returnsCorrectGenerationAmount() {
    BuildingEntity buildingLevel5 = new BuildingEntity(5L, BuildingType.FARM, 5, 200,
        1L, 2L);
    ResourceEntity foodResource = new ResourceEntity(1L, ResourceType.FOOD, 100,
        50, 1L, new KingdomEntity());
    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");
    Mockito.when(env.getProperty("resourceEntity.gold")).thenReturn("10");

    int amount = resourceServiceImpl.calculateNewResourceGeneration(foodResource, buildingLevel5);

    Assert.assertEquals(80, amount);
  }

  @Test
  public void calculateNewResourceGeneration_gold_returnsCorrectGenerationAmount() {
    BuildingEntity buildingLevel5 = new BuildingEntity(5L, BuildingType.MINE, 5, 200,
        1L, 2L);
    ResourceEntity goldResource = new ResourceEntity(1L, ResourceType.GOLD, 100,
        50, 1L, new KingdomEntity());
    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");
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
  public void updateResourceGeneration_passesCorrectArgumentsToScheduleResourceUpdateMethod(){
    resourceServiceImpl = Mockito.spy(resourceServiceImpl);
    KingdomEntity kingdom = new KingdomEntity();
    ResourceEntity resource = new ResourceEntity(1L, ResourceType.GOLD, 100, 100, 999L, kingdom);
    BuildingEntity building = new BuildingEntity(10L, BuildingType.MINE, 1, 100,
        10L, 1000L);
    Timer mockTimer = Mockito.mock(Timer.class);

    Mockito.doReturn(resource).when(resourceServiceImpl).findResourceBasedOnBuildingType(kingdom, building.getType());
    Mockito.doReturn(100).when(resourceServiceImpl).calculateNewResourceGeneration(resource, building);
    Mockito.doReturn(mockTimer).when(resourceServiceImpl).createNewTimer();

    ResourceEntity resourceToBeUpdated = resourceServiceImpl.updateResourceGeneration(kingdom,building);

    Mockito.verify(mockTimer).schedule(resourceTimerTaskCaptor.capture(), delayCaptor.capture());

    ResourceTimerTask task = resourceTimerTaskCaptor.getValue();
    ResourceEntity passedResource = task.getResource();
    Integer passedGeneration = task.getGeneration();
    BuildingEntity passedBuilding = task.getBuilding();

    Assert.assertEquals(resource,passedResource);
    Assert.assertEquals(100,passedGeneration.intValue());
    Assert.assertEquals(building,passedBuilding);
  }

}