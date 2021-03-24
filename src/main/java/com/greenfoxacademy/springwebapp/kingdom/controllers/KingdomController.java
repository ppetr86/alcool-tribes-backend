package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(KingdomController.URI)
public class KingdomController {

  public static final String URI = "/kingdom";

  private final KingdomService kingdomService;

  private final ResourceService resourceService;

  @GetMapping("/{id}")
  public ResponseEntity<Object> getKingdomByID(@PathVariable Long id) throws IdNotFoundException {
    return ResponseEntity.ok(kingdomService.entityToKingdomResponseDTO(id));
  }

  @GetMapping("/resources")
  public ResponseEntity<?> getKingdomResources(Authentication authentication) {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    ResourceListResponseDTO allResources = resourceService.convertKingdomResourcesToListResponseDTO(kingdom);
    return ResponseEntity.ok().body(allResources);
  }

  @PostMapping
  public ResponseEntity<?> updateKingdomByName(Authentication auth,
                                               @RequestBody @Valid KingdomNameDTO nameDTO) {
    KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    return ResponseEntity.ok(kingdomService.changeKingdomName(kingdom, nameDTO));
  }
}
