package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;


public class ResourceServisTest {
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
  public void findResourceBasedOnBuildingType_FarmReturnsFood(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    ResourceEntity food = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.FOOD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.FARM);

    Assert.assertEquals(food,resource);
  }

  @Test
  public void findResourceBasedOnBuildingType_MineReturnsGold(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    ResourceEntity gold = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.GOLD)
        .findFirst().orElse(null);

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.MINE);

    Assert.assertEquals(gold,resource);
  }

  @Test
  public void findResourceBasedOnBuildingType_AcademyReturnsNull(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));

    ResourceEntity resource = resourceService.findResourceBasedOnBuildingType(kingdom, BuildingType.ACADEMY);

    Assert.assertEquals(null,resource);
  }

  @Test
  public void calculateNewResourceGeneration_food_returnsCorrectGenerationAmount(){
    BuildingEntity buildingLevel5 = new BuildingEntity(5L,BuildingType.FARM,5,200,
        1L,2L);
    ResourceEntity foodResource =  new ResourceEntity(1L, ResourceType.FOOD,100,
        50,1L, new KingdomEntity());
    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");
    Mockito.when(env.getProperty("resourceEntity.gold")).thenReturn("10");

    int amount = resourceServiceImpl.calculateNewResourceGeneration(foodResource, buildingLevel5);

    Assert.assertEquals(80, amount);
  }

  @Test
  public void calculateNewResourceGeneration_gold_returnsCorrectGenerationAmount(){
    BuildingEntity buildingLevel5 = new BuildingEntity(5L,BuildingType.MINE,5,200,
        1L,2L);
    ResourceEntity goldResource =  new ResourceEntity(1L, ResourceType.GOLD,100,
        50,1L, new KingdomEntity());
    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");
    Mockito.when(env.getProperty("resourceEntity.gold")).thenReturn("10");

    int amount = resourceServiceImpl.calculateNewResourceGeneration(goldResource, buildingLevel5);

    Assert.assertEquals(110, amount);
  }

  @Test
  public void calculateResourcesUntilBuildingIsFinished_correctResult(){
    BuildingEntity building = new BuildingEntity(5L,BuildingType.MINE,5,200,1L,
        500L);
    ResourceEntity resource =  new ResourceEntity(1L, ResourceType.GOLD,100,50,
        300L, new KingdomEntity());
    Mockito.when(timeService.getTimeBetween(500L,300L)).thenReturn(200);

    int resourcesGenerated = resourceServiceImpl.calculateResourcesUntilBuildingIsFinished(building, resource);

    Assert.assertEquals(166, resourcesGenerated);
  }

}
