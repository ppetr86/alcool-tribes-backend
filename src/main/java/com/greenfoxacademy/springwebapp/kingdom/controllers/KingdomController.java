package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceListResponseDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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



  /*@GetMapping("/resources")
  public ResponseEntity<?> getKingdomResources(Authentication authentication) {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<ResourceResponseDTO> listOfResources = resourceService.findByKingdomId(kingdom.getId());
    ResourceListResponseDTO allResources = new ResourceListResponseDTO(listOfResources);
    return ResponseEntity.ok().body(allResources);
  }*/

  @GetMapping("/resources")
  public ResponseEntity<?> getKingdomResources(Authentication authentication) {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    ResourceListResponseDTO allResources = resourceService.convertKingdomRessourcesToListResponseDTO(kingdom);
    return ResponseEntity.ok().body(allResources);
  }
}
