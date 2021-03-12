package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocationService {

  LocationEntity save(LocationEntity entity);

  LocationEntity defaultLocation(KingdomEntity kingdom);

  List<LocationEntity> findAll();

  void generateNDesertsAndJungles(int n);
}
