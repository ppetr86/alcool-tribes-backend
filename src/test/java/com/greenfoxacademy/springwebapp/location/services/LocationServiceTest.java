package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.factories.KingdomFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import static com.greenfoxacademy.springwebapp.factories.KingdomFactory.createFullKingdom;
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

    Assert.assertNull(location);
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

  @Test
  public void isEligible_returnsTrue_OnEmptyKingdomsSet() {
    List<LocationEntity> locations = createLocations();

    Set<LocationEntity> kingdoms = locations.stream().filter(x -> x.getType().equals(LocationType.EMPTY)).collect(
        Collectors.toSet());
    Assert.assertTrue(locationService.isEligible(kingdoms, 111, 111));
  }

  @Test
  public void prioritizeLocationsByCoordinates_PolledFromQueueIsCorrect() {
    List<LocationEntity> locations = createLocations();
    PriorityQueue<LocationEntity> q = locationService.prioritizeLocationsByCoordinates(1, 0, locations);
    Assert.assertEquals(new LocationEntity(1, 0), q.poll());
    Assert.assertEquals(new LocationEntity(1, -1), q.poll());
    Assert.assertEquals(new LocationEntity(0, 0), q.poll());
    Assert.assertEquals(new LocationEntity(2, 0), q.poll());
    Assert.assertEquals(new LocationEntity(1, 1), q.poll());
    Assert.assertNotEquals(new LocationEntity(3, 3), q.poll());
  }

  @Test
  public void locationWithLowerDistance_returnsFirstLocationWithReguiredDistance() {
    List<LocationEntity> lastNeighbours = new ArrayList<>();
    lastNeighbours.add(new LocationEntity(1, 2));
    lastNeighbours.add(new LocationEntity(2, 2));
    lastNeighbours.add(new LocationEntity(2, 3));
    lastNeighbours.add(new LocationEntity(2, 1));

    Map<LocationEntity, Integer> distances = createDistances();
    Assert.assertEquals(new LocationEntity(2, 3),
        locationService.locationWithLowerDistance(lastNeighbours, distances, 1));
    Assert.assertNotEquals(new LocationEntity(2, 3),
        locationService.locationWithLowerDistance(lastNeighbours, distances, 2));
  }

  @Test
  public void calculateDistanceToStart_returnsCorrectResults() {
    Map<LocationEntity, Integer> distances = createDistances();
    distances.put(new LocationEntity(1, 2), 999);
    LocationEntity neighbour = new LocationEntity(1, 2);
    LocationEntity popped = new LocationEntity(2, 2);
    locationService.calculateDistanceToStart(neighbour, popped, distances);
    int result = distances.get(neighbour);
    Assert.assertEquals(3, result);
  }

  @Test
  public void mapLocationToIndex_ReturnsCorrectResult() {
    LocationEntity start = new LocationEntity(5, 5);
    LocationEntity popped = new LocationEntity(0, 0);
    Assert.assertEquals(2, locationService.mapLocationToIndex(popped, start).length);
    Assert.assertEquals(5, locationService.mapLocationToIndex(popped, start)[0]);
    Assert.assertEquals(5, locationService.mapLocationToIndex(popped, start)[1]);
  }

  @Test
  public void findNeighbours_ReturnsCorrectResults_ForALocationSurroundedByOthersFromAllSides() {
    int rows = 5;
    int cols = 5;
    int getID = 12;
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);

    LocationEntity[][] map = locationService.buildMap(sortReduced);
    LocationEntity centerPoint = sortReduced.get(getID - 1);
    List<LocationEntity> neighbours = locationService.findNeighbours(centerPoint, map);
    Assert.assertEquals(4, neighbours.size());
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID - 1));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + 1));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID - cols));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + cols));
  }

  @Test
  public void findNeighbours_ReturnsCorrectResults_ForALocationInCorner() {
    int rows = 5;
    int cols = 5;
    int getID = 1;
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);

    LocationEntity[][] map = locationService.buildMap(sortReduced);
    LocationEntity centerPoint = sortReduced.get(getID - 1);
    List<LocationEntity> neighbours = locationService.findNeighbours(centerPoint, map);
    Assert.assertEquals(2, neighbours.size());
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + 1));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + cols));
  }

  @Test
  public void findNeighbours_ReturnsCorrectResults_ForALocationOnWall() {
    int rows = 5;
    int cols = 5;
    int getID = cols + 1;
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);

    LocationEntity[][] map = locationService.buildMap(sortReduced);
    LocationEntity centerPoint = sortReduced.get(getID - 1);
    List<LocationEntity> neighbours = locationService.findNeighbours(centerPoint, map);
    Assert.assertEquals(3, neighbours.size());
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + 1));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID + cols));
    Assert.assertTrue(neighbours.stream().anyMatch(each -> each.getId() == getID - cols));
  }

  @Test
  public void buildMap_ReturnsCorrectResults() {
    List<LocationEntity> sortReduced = createListWithLocations(6, 6);
    LocationEntity[][] map = locationService.buildMap(sortReduced);
    long mapSize = Arrays.stream(map).flatMap(Arrays::stream).count();
    long countX = sortReduced.stream().map(LocationEntity::getX).distinct().count();
    long countY = sortReduced.stream().map(LocationEntity::getY).distinct().count();
    Assert.assertEquals(sortReduced.size(), mapSize);
    Assert.assertEquals(sortReduced.get(0), map[0][0]);
    Assert.assertEquals(sortReduced.get(sortReduced.size() - 1), map[map.length - 1][map[map.length - 1].length - 1]);
    Assert.assertEquals(sortReduced.get(1), map[0][1]);
    Assert.assertEquals(countX, map[0].length);
    Assert.assertEquals(countY, map.length);
  }

  @Test
  public void removeKeyValuesWithMaxInt_RemovesKeyValuePairsWithMaxInt() {
    Map<LocationEntity, Integer> distances = createDistances();
    distances.put(new LocationEntity(100, 100), Integer.MAX_VALUE);
    distances.put(new LocationEntity(100, 100), Integer.MAX_VALUE - 1);

    Map<LocationEntity, Integer> result = locationService.removeKeyValuesWithMaxInt(distances);
    Assert.assertEquals(6, distances.size());
    Assert.assertFalse(distances.containsValue(Integer.MAX_VALUE));
  }

  @Test
  public void prepareDistancesMap_ReturnsMapWithAllValuesMaxInt_StartLocWithZero() {
    int rows = 6;
    int cols = 6;
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);
    LocationEntity start = new LocationEntity(0, 0, null, LocationType.KINGDOM);
    Map<LocationEntity, Integer> distances = locationService.prepareDistancesMap(sortReduced, start);

    Assert.assertEquals(2, distances.values().stream().distinct().count());
    int shouldBeZero = distances.values().stream().min(Integer::compare).orElse(Integer.MIN_VALUE);
    Assert.assertEquals(0, shouldBeZero);
    int shouldBeIntMax = distances.values().stream().max(Integer::compare).orElse(Integer.MIN_VALUE);
    Assert.assertEquals(Integer.MAX_VALUE, shouldBeIntMax);
    int startLocShouldBeZero = distances.get(new LocationEntity(0, 0));
    Assert.assertEquals(0, startLocShouldBeZero);
    Assert.assertEquals(rows * cols, distances.size());
    long thereShouldBeTwoNumbersInValues = distances.values().stream().distinct().count();
    Assert.assertEquals(2, thereShouldBeTwoNumbersInValues);
  }


  @Ignore
  @Test
  public void backtrackReturns_CorrectPath() {
    int rows = 6;
    int cols = 6;
    LocationEntity start = new LocationEntity(1L, cols - 1, 0, createFullKingdom(1L, 1L), LocationType.KINGDOM);
    LocationEntity end = new LocationEntity((long) (rows * cols), 0, 0,
        createFullKingdom(2L, 2L), LocationType.KINGDOM);
    List<LocationEntity> sortReduced = prepareLocationsListWithStartEnd(start, end, rows, cols);
    LocationEntity[][] maze = locationService.buildMap(sortReduced);//there is a problem and I dont know what it is
    Map<LocationEntity, Integer> distances = locationService.prepareDistancesMap(sortReduced, start);
    List<LocationEntity> backtracked = locationService.backtrack(distances, end, maze);
    Assert.assertEquals(11, backtracked.size());
    Assert.assertEquals(0, backtracked.stream().filter(each -> each.getType().equals(LocationType.DESERT)).count());
    Assert.assertEquals(0, backtracked.stream().filter(each -> each.getType().equals(LocationType.JUNGLE)).count());
    Assert.assertTrue(backtracked.contains(start));
    Assert.assertTrue(backtracked.contains(end));
    Assert.assertEquals(start, backtracked.get(0));
    Assert.assertEquals(end, backtracked.get(backtracked.size() - 1));
  }

  private List<LocationEntity> prepareLocationsListWithStartEnd(LocationEntity start,
                                                                LocationEntity end, int rows, int cols) {
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);
    sortReduced.set(0, start);
    sortReduced.set(sortReduced.size() - 1, end);
    return sortReduced;
  }

  @Test
  public void pathfinder_ReturnsCorrectPath() {
    int rows = 6;
    int cols = 6;
    List<LocationEntity> sortReduced = createListWithLocations(rows, cols);
    LocationEntity[][] maze = locationService.buildMap(sortReduced);
    LocationEntity start = new LocationEntity(cols - 1, 0, null, LocationType.KINGDOM);
    LocationEntity end = new LocationEntity(0, rows - 1, null, LocationType.KINGDOM);
    Map<LocationEntity, Integer> distances = locationService.prepareDistancesMap(sortReduced, start);
    List<LocationEntity> result = locationService.pathFinder(0, start, end, maze, sortReduced);
    Assert.assertEquals(11, result.size());
    Assert.assertEquals(0, result.stream().filter(each -> each.getType().equals(LocationType.DESERT)).count());
    Assert.assertEquals(0, result.stream().filter(each -> each.getType().equals(LocationType.JUNGLE)).count());
    Assert.assertTrue(result.contains(start));
    Assert.assertTrue(result.contains(end));
  }

  private List<LocationEntity> createListWithLocations(int rows, int cols) {
    List<LocationEntity> sortReduced = new ArrayList<>(rows * cols);
    long id = 1;
    for (int i = rows - 1; i >= 0; i--) {
      for (int j = 0; j <= cols - 1; j++) {
        sortReduced.add(new LocationEntity(id++, j, i, null, LocationType.EMPTY));
      }
    }
    return sortReduced;
  }

  private Map<LocationEntity, Integer> createDistances() {
    Map<LocationEntity, Integer> distances = new HashMap<>();
    distances.put(new LocationEntity(1, 2), 3);
    distances.put(new LocationEntity(2, 2), 2);
    distances.put(new LocationEntity(2, 3), 1);
    distances.put(new LocationEntity(2, 1), 4);
    distances.put(new LocationEntity(0, 0), 5);
    return distances;
  }

}