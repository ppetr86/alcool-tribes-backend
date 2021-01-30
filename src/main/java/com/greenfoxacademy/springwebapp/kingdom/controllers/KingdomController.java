package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(KingdomController.KINGDOMGET)
public class KingdomController {

  public static final String KINGDOMGET = "/kingdom/{id}";

  private final KingdomService kingdomService;

  @GetMapping
  public ResponseEntity getKingdomByID(@PathVariable Long id){
    KingdomEntity k = kingdomService.findByID(id);

    if (k!=null) return
  }
}
