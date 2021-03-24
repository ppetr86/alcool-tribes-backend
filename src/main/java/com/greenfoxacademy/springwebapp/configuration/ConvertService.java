package com.greenfoxacademy.springwebapp.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConvertService {

  private final ObjectMapper mapper = new ObjectMapper();

  public String objectToJson(Object o) {
    try {
      return mapper.writeValueAsString(o);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return "failed object to json conversion";
  }
}
