package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler({InvalidBuildingTypeException.class, TownhallLevelException.class})
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          Exception ex) {
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(MissingParameterException.class)
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          MissingParameterException ex) {
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotEnoughResourceException.class)
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          NotEnoughResourceException ex) {
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.CONFLICT);
  }
}