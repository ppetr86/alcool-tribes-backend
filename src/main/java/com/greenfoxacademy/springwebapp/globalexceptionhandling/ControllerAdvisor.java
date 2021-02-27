package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {

    List<FieldError> errors = ex.getBindingResult().getFieldErrors();
    if (errors.size() > 1) {
      //filter by the annotation type
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < errors.size(); i++) {
        if (i == 0) {
          sb.append(errors.get(i).getField().substring(0, 1).toUpperCase());
          sb.append(errors.get(i).getField().substring(1)).append(" and ");
        } else {
          String msgCurrent = errors.get(i).getDefaultMessage();
          if (!sb.toString().contains(msgCurrent.substring(0, msgCurrent.indexOf(" ")))) {
            sb.append(errors.get(i).getField());
              if (i < errors.size() - 1) {
                  sb.append(" and ");
              } else {
                  sb.append(" are required.");
              }
          }
        }
      }
      return new ResponseEntity<>(new ErrorDTO(sb.toString()), HttpStatus.BAD_REQUEST);
    }

      if (errors.get(0).getDefaultMessage().equals("Password must be 8 characters.")) {
          return new ResponseEntity<>(new ErrorDTO("Password must be 8 characters."), HttpStatus.NOT_ACCEPTABLE);
      }

    //covers for missing type, password required, username required
    return new ResponseEntity<>(new ErrorDTO(errors.get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
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

  @ExceptionHandler(UsernameIsTakenException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(UsernameIsTakenException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.CONFLICT);
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

  @ExceptionHandler(IncorrectUsernameOrPwdException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(IncorrectUsernameOrPwdException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(NotVerifiedRegistrationException.class)
  public ResponseEntity<ErrorDTO> handleExceptions(NotVerifiedRegistrationException ex) {
    log.error(ex.getMessage());
    return new ResponseEntity<>(new ErrorDTO(ex.getMessage()), HttpStatus.UNAUTHORIZED);
  }
}