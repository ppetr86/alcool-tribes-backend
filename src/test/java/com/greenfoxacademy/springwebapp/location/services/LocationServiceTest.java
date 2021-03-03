package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.LocationFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class LocationServiceTest {

  private LocationService locationService;
  private LocationRepository locationRepository;

  @Before
  public void setUp() {
    locationRepository = Mockito.mock(LocationRepository.class);
    locationService = new LocationServiceImpl(locationRepository);
  }

  @Test
  public void defaultLocation_generatesUnoccupiedLocationInRangePlusMinus100() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(11L);
    List<LocationEntity> occupiedLocations = LocationFactory.createOccupiedLocations();

    for (int i = 0; i < 150; i++) {
      LocationEntity startingLocation = locationService.defaultLocation(kingdom);
      Assert.assertFalse(occupiedLocations.contains(startingLocation));
      Assert.assertEquals(startingLocation.getType(), LocationType.KINGDOM);
      Assert.assertTrue(startingLocation.getX() >= -100 && startingLocation.getX() <= 100);
      Assert.assertTrue(startingLocation.getY() >= -100 && startingLocation.getY() <= 100);
      System.out.println(startingLocation.getX());
      System.out.println(startingLocation.getY());
    }
  }
}
