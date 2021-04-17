package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface LocationService {

  Integer distanceBetweenKingdomsWithoutObstacles(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom);

  LocationEntity save(LocationEntity entity);

  LocationEntity assignKingdomLocation(KingdomEntity kingdom);

  boolean isTypeChangeableToTarget(LocationEntity first, Set<LocationEntity> kingdoms);

  List<LocationEntity> findShortestPath(KingdomEntity start, KingdomEntity end);
}
