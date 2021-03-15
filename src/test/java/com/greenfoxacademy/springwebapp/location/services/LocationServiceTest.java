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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class LocationServiceTest {

  private LocationServiceImpl locationService;
  private LocationRepository locationRepository;
  private Comparator<LocationEntity> comparator;

  @Before
  public void setUp() {
    locationRepository = Mockito.mock(LocationRepository.class);
    locationService = new LocationServiceImpl(locationRepository);
    comparator = new LocationServiceImpl.LocationComparator(0, 0);
    locationService = Mockito.spy(locationService);
  }

  @Test
  public void defaultLocation_setsKingdomToLocation() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 0, null, LocationType.EMPTY),
        new LocationEntity(0, 1, null, LocationType.EMPTY)
    );
    mockLocations(locations);
    Mockito.doReturn(true).when(locationService).isEligibleToBecomeKingdom(any(), any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.defaultLocation(kingdom);

    Assert.assertEquals(kingdom, location.getKingdom());
  }

  @Test
  public void defaultLocation_savesSelectedLocationToDB() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 0, null, LocationType.EMPTY),
        new LocationEntity(0, 1, null, LocationType.EMPTY)
    );
    mockLocations(locations);
    Mockito.doReturn(true).when(locationService).isEligibleToBecomeKingdom(any(), any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.defaultLocation(kingdom);

    Mockito.verify(locationRepository).save(location);
  }

  @Test
  public void defaultLocation_doesntSetKingdomNextToAnotherKingdom() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 0, null, LocationType.EMPTY),
        new LocationEntity(0, 1, null, LocationType.KINGDOM),
        new LocationEntity(0, 10, null, LocationType.EMPTY)
    );
    mockLocations(locations);
    Mockito.doReturn(false).when(locationService)
        .isEligibleToBecomeKingdom(locations.get(0), LocationType.KINGDOM, locations);
    Mockito.doReturn(false).when(locationService)
        .isEligibleToBecomeKingdom(locations.get(1), LocationType.KINGDOM, locations);
    Mockito.doReturn(true).when(locationService)
        .isEligibleToBecomeKingdom(locations.get(2), LocationType.KINGDOM, locations);
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.defaultLocation(kingdom);

    Assert.assertEquals(locations.get(2), location);
  }

  @Ignore
  @Test
  public void defaultLocation_setKingdomToNearestProperLocation() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 2, null, LocationType.EMPTY),
        new LocationEntity(0, -1, null, LocationType.EMPTY)
    );
    mockLocations(locations);
    Mockito.doReturn(false).when(locationService)
        .isEligibleToBecomeKingdom(any(), any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.defaultLocation(kingdom);

    Assert.assertEquals(locations.get(1), location);
  }

  @Ignore
  @Test
  public void defaultLocation_doesntSetKingdom_when_NoFreeLocation() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 2, null, LocationType.KINGDOM)
    );
    mockLocations(locations);
    Mockito.doReturn(false).when(locationService)
        .isEligibleToBecomeKingdom(any(), any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.defaultLocation(kingdom);

    Assert.assertEquals(null, location);
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
    Mockito.when(locationService.isEligibleToBecomeKingdom(first, LocationType.KINGDOM, emptyLocations)).thenReturn(false);

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

  private void mockLocations(List<LocationEntity> locations) {
    PriorityQueue<LocationEntity> orderedLocations = new PriorityQueue<>(comparator);
    orderedLocations.addAll(locations);
    Mockito.when(locationRepository.findAllLocationsByTypeIs(LocationType.EMPTY))
        .thenReturn(locations);
    Mockito.doReturn(orderedLocations).when(locationService)
        .prioritizeLocationsByCoordinates(0, 0, locations);
  }
}
