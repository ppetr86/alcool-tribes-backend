package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.factories.LocationFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;

public class LocationServiceTest {

  private LocationServiceImpl locationService;
  private LocationRepository locationRepository;

  @Before
  public void setUp() {
    locationRepository = Mockito.mock(LocationRepository.class);
    locationService = new LocationServiceImpl(locationRepository);
  }

  @Ignore
  @Test
  public void defaultLocation_hasNoNeighbouringLocationTypeKingdom() {
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(11L);
    List<LocationEntity> emptyLocations = LocationFactory.createOccupiedLocations();
    Mockito.when(locationRepository.findAllLocationsByTypeIs(LocationType.EMPTY)).thenReturn(emptyLocations);
    /*locationService = Mockito.spy(locationService);*/
    LocationEntity startingLocation = locationService.defaultLocation(kingdom);
    LocationEntity first =
        emptyLocations.stream().filter(x -> x.getX() == 0 && x.getY() == 0).findFirst().orElse(null);
    Mockito.when(locationRepository.findByXIsAndYIs(anyInt(), anyInt())).thenReturn(new LocationEntity());
    Mockito.when(locationService.isEligibleToBecomeKingdom(first, LocationType.KINGDOM)).thenReturn(false);

    /*Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX() - 1, firstInQueue.getY()));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX() + 1, firstInQueue.getY()));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX(), firstInQueue.getY() + 1));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX(), firstInQueue.getY() - 1));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX() - 1, firstInQueue.getY()));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX() + 1, firstInQueue.getY()));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX(), firstInQueue.getY() + 1));
    Mockito.when(locationRepository.findByXIsAndYIs(startingLocation.getX(), firstInQueue.getY() - 1));*/

    /*LocationEntity toLeft = emptyLocations.stream()
        .filter(x -> x.getX() - 1 == startingLocation.getX() && x.getY() == startingLocation.getY()).findFirst()
        .orElse(null);
    LocationEntity toRight = emptyLocations.stream()
        .filter(x -> x.getX() + 1 == startingLocation.getX() && x.getY() == startingLocation.getY()).findFirst()
        .orElse(null);
    LocationEntity toUp = emptyLocations.stream()
        .filter(x -> x.getX() == startingLocation.getX() && x.getY() - 1 == startingLocation.getY()).findFirst()
        .orElse(null);
    LocationEntity toDown = emptyLocations.stream()
        .filter(x -> x.getX() == startingLocation.getX() && x.getY() + 1 == startingLocation.getY()).findFirst()
        .orElse(null);*/


    Assert.assertTrue(emptyLocations.contains(startingLocation));
    /*Assert.assertFalse(toLeft.getType().equals(LocationType.KINGDOM));
    Assert.assertFalse(toRight.getType().equals(LocationType.KINGDOM));
    Assert.assertFalse(toUp.getType().equals(LocationType.KINGDOM));
    Assert.assertFalse(toDown.getType().equals(LocationType.KINGDOM));*/
    Assert.assertEquals(startingLocation.getType(), LocationType.KINGDOM);

  }
}
