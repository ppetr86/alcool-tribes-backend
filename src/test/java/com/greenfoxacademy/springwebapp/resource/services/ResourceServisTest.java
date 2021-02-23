package com.greenfoxacademy.springwebapp.resource.services;

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

  @Before
  public void setUp() {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    timeService = Mockito.mock(TimeService.class);
    env = Mockito.mock(Environment.class);
    resourceService = new ResourceServiceImpl(resourceRepository, timeService, env);
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
}
