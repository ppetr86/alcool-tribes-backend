package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.Coordinate;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface LocationService {

  LocationEntity save(LocationEntity entity);

  LocationEntity assignKingdomLocation(KingdomEntity kingdom);

  List<Coordinate> findShortestPathV99(KingdomEntity start, KingdomEntity end);

  boolean isTypeChangeableToTarget(LocationEntity first, Set<LocationEntity> kingdoms);
}
