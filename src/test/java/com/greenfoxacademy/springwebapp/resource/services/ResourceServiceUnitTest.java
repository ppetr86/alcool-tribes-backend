package com.greenfoxacademy.springwebapp.resource.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.repositories.ResourceRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class ResourceServiceUnitTest {
  private ResourceService resourceService;
  private ResourceRepository resourceRepository;

  @Before
  public void setUp() throws Exception {
    resourceRepository = Mockito.mock(ResourceRepository.class);
    resourceService = new ResourceServiceImpl(resourceRepository);
  }

  @Test
  public void createDefaultResourcesShouldReturnCorrectResources() {
    KingdomEntity kingdomEntity = new KingdomEntity();
    kingdomEntity.setId(1L);

    List<ResourceEntity> result = resourceService.createDefaultResources(kingdomEntity);

    Assert.assertEquals("food", result.get(0).getType().resourceType);

  }
}