package com.greenfoxacademy.springwebapp.location.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import com.greenfoxacademy.springwebapp.location.services.LocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(LocationController.URI)
public class LocationController {

    public static final String URI = "/location";

    private final LocationService locationService;

    @GetMapping("/existskingdom")
    public ResponseEntity<Boolean> existsKingdomInLocations() {
        return ResponseEntity.ok(locationService.existsLocationWhereKingdomIsNotNull());
    }

    @GetMapping("/specifications")
    public ResponseEntity<Object> getKingdomByID(@RequestParam int x, @RequestParam int locationType) throws IdNotFoundException {
        return ResponseEntity.ok(locationService.showMatchesBySpecification(x, LocationType.values()[locationType]));
    }

    @GetMapping("/randommatches")
    public ResponseEntity<LocationEntityDTO[]> showRandomMatchesByClassFieldsSpecifications(String x, String y, String id) {
        return ResponseEntity.ok(locationService.showRandomMatchesByClassFieldsSpecifications(x, y, id));
    }
}