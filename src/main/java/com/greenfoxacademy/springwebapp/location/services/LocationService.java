package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {

  LocationEntity save(LocationEntity entity);
}
