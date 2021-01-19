package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(InvalidBuildingTypeException.class)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
  public ExceptionResponseDTO handleExceptions(InvalidBuildingTypeException ex) {
    return new ExceptionResponseDTO(ex.getMessage());
  }

  /*@ExceptionHandler(InvalidBuildingTypeException.class)
  public ResponseEntity<ExceptionResponseDTO> handleExceptions(
          InvalidBuildingTypeException ex) {
    return new ResponseEntity<>(new ExceptionResponseDTO(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
  }*/

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
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
          MethodArgumentNotValidException ex, HttpHeaders headers,
          HttpStatus status, WebRequest request) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDate.now());
    body.put("status", status.value());

    List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(x -> x.getDefaultMessage())
            .collect(Collectors.toList());

    body.put("errors", errors);

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
