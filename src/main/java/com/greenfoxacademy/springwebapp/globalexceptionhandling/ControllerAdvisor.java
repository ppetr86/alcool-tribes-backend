package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
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

    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    if (errors.size() > 1) {
      return new ResponseEntity<>(new ErrorDTO(createTextFromFieldErrors(errors)), HttpStatus.BAD_REQUEST);
    }

    if (errors.get(0).getDefaultMessage().equals("Password must be 8 characters.")) {
      return new ResponseEntity<>(new ErrorDTO("Password must be 8 characters."), HttpStatus.NOT_ACCEPTABLE);
    }

    //covers for missing type, password required, username required
    return new ResponseEntity<>(new ErrorDTO(errors.get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
  }

  private String createTextFromFieldErrors(List<FieldError> errors) {

    String result = errors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .map(x -> x.toLowerCase().substring(0, x.indexOf(" ")))
        .distinct()
        .sorted()
        .map(each -> each + " and ")
        .collect(Collectors.joining());

    return result.substring(0, 1).toUpperCase() + result.substring(1, result.lastIndexOf(" and")) + " are required.";
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
  public ResponseEntity<ErrorDTO> handleExceptions(ForbiddenActionException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(RuntimeException ex) {
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    if (ex.getMessage().equals("Not verified username.")) {
      status = HttpStatus.UNAUTHORIZED;
    }
    if (ex.getMessage().equals("Username or password is incorrect.")) {
      status = HttpStatus.UNAUTHORIZED;
    }
    if (ex.getMessage().equals("Username is already taken.")) {
      status = HttpStatus.CONFLICT;
    }

    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), status);
  }

}