package com.greenfoxacademy.springwebapp.troop.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(TroopController.URI)
public class TroopController {
  public static final String URI = "/kingdom/troops";

  public ResponseEntity<?> createTroop(@RequestBody @Valid TroopRequestDTO, Authentication auth)
      throws ForbiddenException, InvalidInputException, NotEnoughResourceException {

  }
}
