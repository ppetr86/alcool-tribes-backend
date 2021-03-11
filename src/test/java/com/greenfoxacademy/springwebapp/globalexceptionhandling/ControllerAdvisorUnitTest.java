package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerAdvisorUnitTest {

  private final ControllerAdvisor ca = new ControllerAdvisor();

  @Test
  public void invalidBuildingTypeException_ReturnsNotAcceptableAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptionsNotAcceptable(new InvalidBuildingTypeException());
    Assert.assertEquals(HttpStatus.valueOf(406), result.getStatusCode());
    Assert.assertEquals("Invalid building type",
        result.getBody().getMessage());
  }

  @Test
  public void incorrectUsernameOrPwdException_ReturnsUnauthorizedAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new RuntimeException("Username or password is incorrect."));
    Assert.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    Assert.assertEquals("Username or password is incorrect.", result.getBody().getMessage());
  }

  @Test
  public void notVerifiedRegistrationException_ReturnsUnauthorizedAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new RuntimeException("Not verified username."));
    Assert.assertEquals(HttpStatus.valueOf(401), result.getStatusCode());
    Assert.assertEquals("Not verified username.", result.getBody().getMessage());
  }

  @Test
  public void forbiddenCustomException_ReturnsForbiddenAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleForbiddenException(new ForbiddenActionException());
    Assert.assertEquals(HttpStatus.valueOf(403), result.getStatusCode());
    Assert.assertEquals("Forbidden action", result.getBody().getMessage());
  }


  @Test
  public void usernameIsTakenException_ReturnsForbiddenAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptions(new RuntimeException("Username is already taken."));
    Assert.assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    Assert.assertEquals("Username is already taken.", result.getBody().getMessage());
  }

  @Test
  public void invalidAcademyIdException_ReturnsForbiddenAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptionsNotAcceptable(new InvalidAcademyIdException());
    Assert.assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
    Assert.assertEquals("Not a valid academy id", result.getBody().getMessage());
  }

  ///

  @Test
  public void townhallLevelException_ReturnsNotAcceptableAndCorrectMessage() {
    ResponseEntity<ErrorDTO> result = ca.handleExceptionsNotAcceptable(new TownhallLevelException());
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
}