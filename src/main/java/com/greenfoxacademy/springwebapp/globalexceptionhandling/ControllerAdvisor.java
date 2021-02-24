package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

    List<String> errorList = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

    if (errorList.contains("Username is required.") && errorList.contains("Password is required."))
      return new ResponseEntity<>(new ErrorDTO("Username and password are required."), HttpStatus.CONFLICT);

    /*if (errorList.contains("Password is required."))
      return new ResponseEntity<>(new ErrorDTO("Password is required."), HttpStatus.BAD_REQUEST);

    if (errorList.contains("Username is required."))
      return new ResponseEntity<>(new ErrorDTO("Username is required."), HttpStatus.BAD_REQUEST);*/

    if (errorList.contains("Password must be 8 characters."))
      return new ResponseEntity<>(new ErrorDTO("Password must be 8 characters."), HttpStatus.NOT_ACCEPTABLE);

    //covers for missing type, password required, username required
    return new ResponseEntity<>(new ErrorDTO(errorList.get(0)), HttpStatus.BAD_REQUEST);
  }


  @ExceptionHandler({
          InvalidBuildingTypeException.class,
          TownhallLevelException.class,
          InvalidInputException.class,
          InvalidAcademyIdException.class,
          PasswordMissingOrTooShortException.class})
  public ResponseEntity<ErrorDTO> handleExceptions(Exception ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler({NotEnoughResourceException.class, UsernameIsTakenException.class})
  public ResponseEntity<ErrorDTO> handleExceptions(NotEnoughResourceException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(IdNotFoundException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(IdNotFoundException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ForbiddenCustomException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(ForbiddenCustomException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
  }
}