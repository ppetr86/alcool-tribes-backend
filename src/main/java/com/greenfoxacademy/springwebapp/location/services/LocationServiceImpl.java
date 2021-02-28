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
public class LocationServiceImpl implements LocationService{

  private LocationRepository repo;

  @Override
  public LocationEntity save(LocationEntity entity) {
    return repo.save(entity);
  }

  @Override
  public LocationEntity defaultLocation(KingdomEntity kingdom) {

    boolean isOccupied = true;
    LocationEntity startingLocation = null;
    List<LocationEntity> allLocations = findAll();

    while (isOccupied){
      startingLocation = generateRandomLocation();
      if (!allLocations.contains(startingLocation)) isOccupied = false;
    }
    startingLocation.setKingdom(kingdom);
    startingLocation.setType(LocationType.KINGDOM);

    return startingLocation;
  }

  private LocationEntity generateRandomLocation() {
    LocationEntity result = new LocationEntity();
    result.setX(new Random().nextInt(101) - 102);
    result.setY(new Random().nextInt(101) - 102);
    return result;
  }

  @Override
  public List<LocationEntity> findAll() {
    return repo.findAll();
  }


}
