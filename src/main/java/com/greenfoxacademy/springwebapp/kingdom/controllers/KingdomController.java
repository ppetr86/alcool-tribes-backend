package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(KingdomController.URI)
public class KingdomController {

  public static final String URI = "/kingdom";

  private final KingdomService kingdomService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getKingdomByID(@PathVariable Long id) throws IdNotFoundException {
    return ResponseEntity.ok(kingdomService.entityToKingdomResponseDTO(id));
  }
}