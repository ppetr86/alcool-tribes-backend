package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.Node;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Lists;

import java.util.List;

@Service
public interface LocationService {

  LocationEntity save(LocationEntity entity);

  LocationEntity defaultLocation(KingdomEntity kingdom);

  List<LocationEntity> findAll();

  void generate50DesertsAnd50Jungles();

  Node[][] findShortestPathV99(KingdomEntity start, KingdomEntity end);

}
