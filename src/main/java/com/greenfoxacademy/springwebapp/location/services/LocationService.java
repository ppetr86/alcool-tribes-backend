package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {

  LocationEntity save(LocationEntity entity);

  LocationEntity assignKingdomLocation(KingdomEntity kingdom);

  boolean isTypeChangeableToTarget(LocationEntity first, LocationType targetType, List<LocationEntity> kingdoms);
}
