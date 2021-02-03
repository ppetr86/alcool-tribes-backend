package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@Slf4j
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler({InvalidBuildingTypeException.class, TownhallLevelException.class})
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          Exception ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(MissingParameterException.class)
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          MissingParameterException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotEnoughResourceException.class)
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          NotEnoughResourceException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.CONFLICT);
  }
}
