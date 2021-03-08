package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
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
  private KingdomService kingdomService;
  private Environment env;

  @Before
  public void setUp() throws Exception {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    timeService = Mockito.mock(TimeService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    env = Mockito.mock(Environment.class);
    resourceService = new ResourceServiceImpl(resourceRepository, timeService, kingdomService, env);
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
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange100(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndHighAmount());
    int amountChange = 100;

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(0).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(1L, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange100(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndLowAmount());

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(999L);

    boolean result = resourceService.hasResourcesForBuilding(1L, 100);

    Assert.assertFalse(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange150(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndHighAmount());
    int amountChange = 150;

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(1L, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange150(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndLowAmount());
    int amountChange = 150;

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(1L, amountChange);

    Assert.assertFalse(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnTrueIfAmountChange200(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndHighAmount());
    int amountChange = 200;

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(1L, amountChange);

    Assert.assertTrue(result);
  }

  @Test
  public void hasResourcesForBuildingShouldReturnFalseIfAmountChange200(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(1L);
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataAndLowAmount());
    int amountChange = 200;

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(timeService.getTime()).thenReturn(kingdom.getResources().get(1).getUpdatedAt());

    boolean result = resourceService.hasResourcesForBuilding(1L, amountChange);

    Assert.assertFalse(result);
  }
}