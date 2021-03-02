package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class ResourceServiceTest {
  private ResourceService resourceService;
  private ResourceRepository resourceRepository;
  private TimeService timeService;
  private KingdomService kingdomService;

  @Before
  public void setUp() throws Exception {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    timeService = Mockito.mock(TimeService.class);
    kingdomService = Mockito.mock(KingdomService.class);
    resourceService = new ResourceServiceImpl(resourceRepository, timeService, kingdomService);
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
}