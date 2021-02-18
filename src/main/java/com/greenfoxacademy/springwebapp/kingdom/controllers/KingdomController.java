package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class KingdomController {
  private ResourceService resourceService;

  public KingdomController(ResourceService resourceService) {
    this.resourceService = resourceService;
  }


  @GetMapping("/kingdom/resources")
  public ResponseEntity<?> getKingdomResources(Authentication authentication){
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<ResourceResponseDTO> listOfResources = resourceService.findByKingdomId(kingdom.getId());
    ResourceListResponseDTO allResources = new ResourceListResponseDTO(listOfResources);
    return ResponseEntity.ok().body(allResources);
  }
}
