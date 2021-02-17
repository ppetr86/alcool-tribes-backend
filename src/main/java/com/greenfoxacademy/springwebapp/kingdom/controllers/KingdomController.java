package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KingdomController {
  private ResourceService resourceService;
  private KingdomService kingdomService;

  public KingdomController(ResourceService resourceService,
                           KingdomService kingdomService) {
    this.resourceService = resourceService;
    this.kingdomService = kingdomService;
  }

  @GetMapping("/kingdom/resources")
  public ResponseEntity<ResourceListResponseDTO> getKingdomResources(Authentication authentication){
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    ResourceListResponseDTO allResources = resourceService.resourcesToListDTO(kingdom);
    return ResponseEntity.ok().body(allResources);
  }
}
