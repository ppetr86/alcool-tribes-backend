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

import java.util.List;

public class ResourceServiceTest {
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

    Assert.assertNull(resource);
  }

  @Test
  public void calculateNewResourceGeneration_food_returnsCorrectGenerationAmount(){
    BuildingEntity buildingLevel5 = new BuildingEntity(5L,BuildingType.FARM,5,200,
        1L,2L);
    ResourceEntity foodResource = new ResourceEntity(1L, ResourceType.FOOD,100,
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
    ResourceEntity goldResource = new ResourceEntity(1L, ResourceType.GOLD,100,
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

  @Test //version1 - using spy for impl class
  public void updateResourceGeneration_Gold_ShouldReturnCorrectlyUpdatedResource_v1(){
    resourceServiceImpl = Mockito.spy(new ResourceServiceImpl(resourceRepository, timeService, env));

    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    BuildingEntity building = new BuildingEntity(10L,BuildingType.MINE,1,100,
        9L,1029L);

    //gold res. will have these values: amount=100, generation=100, updatedAt=999
    ResourceEntity goldResourceToBeUpdated = kingdom.getResources().stream()
        .filter(a -> a.getType().equals(ResourceType.GOLD))
        .findFirst().get();

    Mockito.doReturn(goldResourceToBeUpdated).when(resourceServiceImpl).findResourceBasedOnBuildingType(kingdom,BuildingType.MINE);
    Mockito.doReturn(10).when(resourceServiceImpl).calculateNewResourceGeneration(goldResourceToBeUpdated,building);
    Mockito.doReturn(50).when(resourceServiceImpl).calculateResourcesUntilBuildingIsFinished(building,goldResourceToBeUpdated);

    Mockito.when(timeService.getTime()).thenReturn(1L);
    Mockito.when(timeService.getTimeBetween(building.getFinishedAt(),1L)).thenReturn(0); //delay = 20s
    Mockito.when(resourceRepository.findById(goldResourceToBeUpdated.getId())).thenReturn(
        java.util.Optional.of(goldResourceToBeUpdated));

    ResourceEntity updatedResource = resourceServiceImpl.updateResourceGeneration(kingdom,building);

    Assert.assertEquals(10,updatedResource.getGeneration().intValue());
  }

  /*  @Test //version2 - not using spy for impl class
  public void updateResourceGeneration_Gold_ShouldReturnCorrectlyUpdatedResource_v2(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setBuildings(BuildingFactory.createDefaultLevel1BuildingsWithAllData());
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(kingdom));
    BuildingEntity building = new BuildingEntity(10L,BuildingType.MINE,1,100,
        9L,1029L);

    //resourceToBeUpdated have these valuses: amount=100, generation=100, updatedAt=999
    ResourceEntity resourceToBeUpdated = kingdom.getResources().stream()
        .filter(a -> a.getType() == ResourceType.GOLD)
        .findFirst().get();

    Mockito.when(timeService.getTime()).thenReturn(1L);
    Mockito.when(timeService.getTimeBetween(building.getFinishedAt(),1L)).thenReturn(20); //delay = 20s
    Mockito.when(resourceRepository.findById(resourceToBeUpdated.getId())).thenReturn(
        java.util.Optional.of(resourceToBeUpdated));

    Mockito.when(env.getProperty("resourceEntity.food")).thenReturn("5");
    Mockito.when(env.getProperty("resourceEntity.gold")).thenReturn("10");

    //variable newResourceGeneration = 110
    //variable resourceToBeUpdated =100
    //variable generatedResourcesInMeantime = 100 + 1*10 + 10 = 120
    //updatedResource: generation = 110, amount = 100+120 = 220, updatedAt = 1029L
    ResourceEntity updatedResource = resourceServiceImpl.updateResourceGeneration(kingdom,building);

    Assert.assertEquals(110,updatedResource.getGeneration().intValue());
    Assert.assertEquals(220,updatedResource.getAmount().intValue());
    Assert.assertEquals(1029,updatedResource.getUpdatedAt().intValue());
  }*/
}