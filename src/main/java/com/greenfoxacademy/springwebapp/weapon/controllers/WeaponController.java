package com.greenfoxacademy.springwebapp.weapon.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponEntity;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponsDTO;
import com.greenfoxacademy.springwebapp.weapon.services.WeaponService;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(WeaponController.URI)
public class WeaponController {

    public static final String URI = "/kingdom/weapons";

    private final WeaponService weaponService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getWeaponByID(@PathVariable UUID id) throws IdNotFoundException {
        return ResponseEntity.ok(weaponService.findById(id));
    }


    @PutMapping
    public ResponseEntity<?> updateWeaponByName(Authentication auth,
                                                @RequestBody @Valid WeaponEntity weapon) {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
        return ResponseEntity.ok(weaponService.changeWeaponName(kingdom, weapon));
    }

    @PostMapping
    public ResponseEntity<?> uploadWeapons(Authentication auth, @RequestBody WeaponsDTO... weaponEntities) throws
            MissingParameterException, IdNotFoundException, ForbiddenActionException {

        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
        return ResponseEntity.ok().body(weaponService.addWeaponsToKingdom(kingdom, weaponEntities));
    }

}