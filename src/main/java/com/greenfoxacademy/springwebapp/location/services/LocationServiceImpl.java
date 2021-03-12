package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

  private final LocationRepository repo;

  @Override
  public LocationEntity save(LocationEntity entity) {
    return repo.save(entity);
  }

  @Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    LocationEntity startingLocation = null;
    Random random = new Random();

    while (startingLocation == null) {
      int randX = random.nextInt(201);
      int randY = random.nextInt(201);

      LocationEntity found = repo.findByXAndY(randX, randY);

      if (found == null || found.getType().equals(LocationType.EMPTY)) {
        if (found !=null) startingLocation.setId(found.getId());
        startingLocation = new LocationEntity();
        startingLocation.setX(randX);
        startingLocation.setY(randY);
        startingLocation.setKingdom(kingdom);
        startingLocation.setType(LocationType.KINGDOM);
      }
    }
    return startingLocation;
  }

 /* @Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    boolean isOccupied = true;
    LocationEntity startingLocation = null;
    List<LocationEntity> allLocations = findAll();

    while (isOccupied) {
      startingLocation = generateRandomLocation();
      if (!allLocations.contains(startingLocation)) {
        isOccupied = false;
      }
    }
    startingLocation.setKingdom(kingdom);
    startingLocation.setType(LocationType.KINGDOM);
    return startingLocation;
  }

  private LocationEntity generateRandomLocation() {
    LocationEntity result = new LocationEntity();
    result.setX(new Random().nextInt(201) - 100);
    result.setY(new Random().nextInt(201) - 100);
    return result;
  }*/

  /*@Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    LocationEntity startingLocation = null;

    while (startingLocation==null){
      Random random = new Random();
      long randomLocationID = (long)(random.nextDouble()*repo.getMaxID());
      LocationEntity found = repo.findById(randomLocationID).orElse(null);
      if (found.getType().equals(LocationType.EMPTY))
        startingLocation = found;
    }

    startingLocation.setKingdom(kingdom);
    startingLocation.setType(LocationType.KINGDOM);
    return startingLocation;
  }

  private List<LocationEntity> findAllByLocationType(LocationType type) {
    return repo.findAllByTypeIs(type);
  }*/

  private LocationEntity generateRandomLocation() {
    LocationEntity result = new LocationEntity();
    result.setX(new Random().nextInt(201) - 100);
    result.setY(new Random().nextInt(201) - 100);
    return result;
  }

  @Override
  public List<LocationEntity> findAll() {
    return repo.findAll();
  }

  @Override
  public void generateNDesertsAndJungles(int n) {

    repo.generateNDesertsAndJungles(n);
  }


}
