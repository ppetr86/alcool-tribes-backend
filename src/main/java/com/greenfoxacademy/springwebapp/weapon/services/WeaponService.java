package com.greenfoxacademy.springwebapp.weapon.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponEntity;
import com.greenfoxacademy.springwebapp.weapon.models.WeaponsDTO;
import java.util.Set;
import java.util.UUID;

public interface WeaponService {
    Set<WeaponEntity> addWeaponsToKingdom(KingdomEntity kingdom, WeaponsDTO... weapons);


    WeaponEntity changeWeaponName(KingdomEntity kingdom, WeaponEntity weapon);


    WeaponEntity findById(UUID id);
}
