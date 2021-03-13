package com.greenfoxacademy.springwebapp.location;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.repositories.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyRunner implements CommandLineRunner {

  @Autowired
  private LocationRepository locationRepository;

  @Override
  public void run(String... args) {
    boolean isGeneratingEmptyLocations = false;
    if (isGeneratingEmptyLocations) {
      locationRepository.deleteAll();
      int counter = 0;
      for (int i = 0; i < 201; i++) {
        for (int j = 0; j < 201; j++) {
          locationRepository.save(new LocationEntity(i*(-1)+100, j-100, null, LocationType.EMPTY));
          counter++;
          if (counter%1000==0) log.info(counter + "created");
        }
      }
      log.info("Empty locations were created to the DB");
    }
  }
}