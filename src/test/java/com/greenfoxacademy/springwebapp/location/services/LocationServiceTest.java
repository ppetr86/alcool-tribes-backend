package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import static com.greenfoxacademy.springwebapp.factories.LocationFactory.createLocations;
import static org.mockito.ArgumentMatchers.any;

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
  public void giveNewKindomALocation_setsKingdomToLocation() {

    mockLocations(createLocations());

    Mockito.doReturn(true).when(locationService).isTypeChangeableToTarget(any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.assignKingdomLocation(kingdom);

    Assert.assertEquals(kingdom, location.getKingdom());
  }

  @Test
  public void giveNewKindomALocation_doesntSetKingdomNextToAnotherKingdom() {
    List<LocationEntity> locations = createLocations();
    Set<LocationEntity> kingdoms = locations.stream().filter(x -> x.getType().equals(LocationType.KINGDOM)).collect(
        Collectors.toSet());

    mockLocations(locations);
    Mockito.doReturn(false).when(locationService)
        .isTypeChangeableToTarget(locations.get(0), kingdoms);
    Mockito.doReturn(false).when(locationService)
        .isTypeChangeableToTarget(locations.get(1), kingdoms);
    Mockito.doReturn(true).when(locationService)
        .isTypeChangeableToTarget(locations.get(2), kingdoms);

    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);
    LocationEntity location = locationService.assignKingdomLocation(kingdom);
    Assert.assertEquals(locations.get(20), location);
  }

  @Test
  public void giveNewKindomALocation_setKingdomToNearestProperLocation() {
    List<LocationEntity> locations = Arrays.asList(
        new LocationEntity(0, 0, null, LocationType.EMPTY),
        new LocationEntity(0, -1, null, LocationType.EMPTY),
        new LocationEntity(0, 1, null, LocationType.EMPTY),
        new LocationEntity(1, 0, null, LocationType.EMPTY),
        new LocationEntity(-1, 0, null, LocationType.EMPTY));

    mockLocations(createLocations());
    Mockito.doReturn(true).when(locationService)
        .isTypeChangeableToTarget(any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.assignKingdomLocation(kingdom);

    Assert.assertEquals(locations.get(0), location);
  }


  @Test(expected = RuntimeException.class)
  public void giveNewKindomALocation_doesntSetKingdom_when_NoFreeLocation() {
    List<LocationEntity> locations = new ArrayList<>();
    locations.add(new LocationEntity(0, 2, null, LocationType.KINGDOM));

    mockLocations(locations);
    Mockito.doReturn(RuntimeException.class).when(locationService)
        .isTypeChangeableToTarget(any(), any());
    KingdomEntity kingdom = KingdomFactory.createKingdomEntityWithId(1L);

    LocationEntity location = locationService.assignKingdomLocation(kingdom);

    Assert.assertEquals(null, location);
  }

  private void mockLocations(List<LocationEntity> locations) {
    PriorityQueue<LocationEntity> orderedLocations = new PriorityQueue<>(comparator);
    orderedLocations.addAll(locations);

    Mockito.when(locationRepository.findAll())
        .thenReturn(locations);

    Mockito.doReturn(orderedLocations).when(locationService)
        .prioritizeLocationsByCoordinates(0, 0, locations);
  }

  @Test
  public void isEligible_returnsFalse() {
    List<LocationEntity> locations = createLocations();

    Set<LocationEntity> kingdoms = locations.stream().filter(x -> x.getType().equals(LocationType.KINGDOM)).collect(
        Collectors.toSet());
    Assert.assertTrue(locationService.isEligible(kingdoms, 0, 0));
  }

  @Test
  public void isEligible_returnsTrue() {
    List<LocationEntity> locations = createLocations();

    Set<LocationEntity> kingdoms = locations.stream().filter(x -> x.getType().equals(LocationType.KINGDOM)).collect(
        Collectors.toSet());
    Assert.assertTrue(locationService.isEligible(kingdoms, 111, 111));
  }


}
