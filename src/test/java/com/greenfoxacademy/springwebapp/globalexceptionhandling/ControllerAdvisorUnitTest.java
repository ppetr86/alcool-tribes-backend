package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerAdvisorUnitTest {

  private final ControllerAdvisor ca = new ControllerAdvisor();

  @Test
  public void invalidBuildingTypeException_ReturnsNOTACCEPTABLE() {
    ResponseEntity<ExceptionResponseDTO> result = ca.handleExceptions(new InvalidBuildingTypeException());
    Assert.assertEquals(HttpStatus.valueOf(406), result.getStatusCode());
    Assert.assertEquals("Invalid building type || Cannot build buildings with higher level than the Townhall",
            result.getBody().getMessage());
  }

  @Test
  public void missingParameterException_ReturnsBADREQUEST() {
    ResponseEntity<ExceptionResponseDTO> result = ca.handleExceptions(new MissingParameterException());
    Assert.assertEquals(HttpStatus.valueOf(400), result.getStatusCode());
    Assert.assertEquals("Missing parameter(s): type!", result.getBody().getMessage());
  }

  @Test
  public void notEnoughResourceException_ReturnsCONFLICT() {
    ResponseEntity<ExceptionResponseDTO> result = ca.handleExceptions(new NotEnoughResourceException());
    Assert.assertEquals(HttpStatus.valueOf(409), result.getStatusCode());
    Assert.assertEquals("Not enough resource", result.getBody().getMessage());
  }

}
