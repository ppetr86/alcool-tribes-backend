package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerAdvisorUnitTest {

  private final ControllerAdvisor ca = new ControllerAdvisor();

  @Test
  public void invalidBuildingTypeException_ReturnsNotAcceptableAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new InvalidBuildingTypeException());
    Assert.assertEquals(HttpStatus.valueOf(406), result.getStatusCode());
    Assert.assertEquals("Invalid building type",
        result.getBody().getMessage());
  }

  @Test
  public void townhallLevelException_ReturnsNotAcceptableAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new TownhallLevelException());
    Assert.assertEquals(HttpStatus.valueOf(406), result.getStatusCode());
    Assert.assertEquals("Cannot build buildings with higher level than the Townhall",
        result.getBody().getMessage());
  }

  @Test
  public void missingParameterException_ReturnsBadRequestAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleBadRequestExceptions(new MissingParameterException("id"));
    Assert.assertEquals(HttpStatus.valueOf(400), result.getStatusCode());
    Assert.assertEquals("Missing parameter(s): id!", result.getBody().getMessage());
  }

  @Test
  public void notEnoughResourceException_ReturnsConflictAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new NotEnoughResourceException());
    Assert.assertEquals(HttpStatus.valueOf(409), result.getStatusCode());
    Assert.assertEquals("Not enough resource", result.getBody().getMessage());
  }

  @Test
  public void idNotFoundExceptionShouldReturnNotFoundAndCorrectMessage() {
    ResponseEntity<ErrorDTO> response = ca.handleExceptions(new IdNotFoundException());
    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", response.getBody().getMessage());
  }

  @Test
  public void forbiddenActionExceptionShouldReturnForbiddenAndCorrectMessage() {
    ResponseEntity<ErrorDTO> response = ca.handleForbiddenException(new ForbiddenActionException());
    Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    Assert.assertEquals("Forbidden action", response.getBody().getMessage());
  }
}
