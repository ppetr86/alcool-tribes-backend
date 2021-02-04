package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//TODO: ALTB-14

@RestController
@AllArgsConstructor
@RequestMapping(KingdomController.KINGDOM_GET)
public class KingdomController {

  public static final String KINGDOM_GET = "/kingdom/{id}";

  private final KingdomService kingdomService;
  //TODO:remove me

  @GetMapping
  public ResponseEntity<Object> getKingdomByID(@PathVariable Long id) {
    KingdomEntity kingdom = kingdomService.findByID(id);
    if (kingdom == null) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Id not found"));
    }
    return ResponseEntity.status(HttpStatus.OK).body(kingdomService.kingdomResponseDTO(kingdom));
  }
}