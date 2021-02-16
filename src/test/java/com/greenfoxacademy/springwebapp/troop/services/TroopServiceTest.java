package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.common.services.TimeService;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopRepository;
import org.junit.Before;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

public class TroopServiceTest {
  private TroopService troopService;
  private ResourceService resourceService;
  private TimeService timeService;
  private TroopRepository troopRepository;
  private Environment env;

  @Before
  public void init() {
    resourceService = Mockito.mock(ResourceService.class);
    timeService = Mockito.mock(TimeService.class);
    troopRepository = Mockito.mock(TroopRepository.class);
    troopService = new TroopServiceImpl(resourceService,timeService,troopRepository,env);
  }

}
