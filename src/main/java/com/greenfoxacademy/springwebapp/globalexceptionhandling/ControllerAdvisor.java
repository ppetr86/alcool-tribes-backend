package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {
    String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return new ResponseEntity<>(new ErrorDTO(errorMessage), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({
      InvalidBuildingTypeException.class,
      TownhallLevelException.class,
      InvalidInputException.class,
      InvalidAcademyIdException.class})
  public ResponseEntity<ErrorDTO> handleExceptions(Exception ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(NotEnoughResourceException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(NotEnoughResourceException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IdNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(IdNotFoundException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.NOT_FOUND);

  }

  @ExceptionHandler(ForbiddenActionException.class)
  public ResponseEntity<ErrorDTO> handleForbiddenException(ForbiddenActionException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(MissingParameterException.class)
  public ResponseEntity<ErrorDTO> handleBadRequestExceptions(Exception ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);
  }
}

