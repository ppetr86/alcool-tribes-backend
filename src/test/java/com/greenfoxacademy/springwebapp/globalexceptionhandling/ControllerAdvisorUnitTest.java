package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import com.greenfoxacademy.springwebapp.buildings.controllers.BuildingsController;
import com.greenfoxacademy.springwebapp.buildings.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.buildings.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidBuildingTypeException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdoms.services.KingdomService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerAdvisorUnitTest {

  @Test
  public void invalidBuildingTypeException_ReturnsNOTACCEPTABLE(){
    InvalidBuildingTypeException exception = new InvalidBuildingTypeException();
  }
}
