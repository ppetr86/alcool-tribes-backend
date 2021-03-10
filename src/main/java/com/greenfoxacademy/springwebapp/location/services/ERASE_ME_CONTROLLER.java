package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ERASE_ME_CONTROLLER {

  private LocationService locationService;
  private KingdomService kingdomService;

  @GetMapping("/erase/{endKingdom}")
  public ResponseEntity<?> getBuildingById(@PathVariable Long endKingdom,
                                           Authentication auth) {

    KingdomEntity start = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
    KingdomEntity end = kingdomService.findByID(endKingdom);

    return ResponseEntity.ok(locationService.findShortestPathV99(start, end));
  }
}
