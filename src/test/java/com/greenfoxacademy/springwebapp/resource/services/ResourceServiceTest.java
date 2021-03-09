package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import java.util.List;

public class ResourceServiceTest {
  private ResourceService resourceService;
  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private Environment env;

  @Before
  public void setUp() throws Exception {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    timeService = Mockito.mock(TimeService.class);
    env = Mockito.mock(Environment.class);
    resourceService = new ResourceServiceImpl(resourceRepository, timeService, env);
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

    Mockito.when(env.getProperty("troop.food")).thenReturn("-5");
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForTroop(kingdom, 25);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForTroopShouldReturnFalse() {
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());

    Mockito.when(env.getProperty("troop.food")).thenReturn("-5");
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForTroop(kingdom, 25);

    Assert.assertFalse(result);
  }
}