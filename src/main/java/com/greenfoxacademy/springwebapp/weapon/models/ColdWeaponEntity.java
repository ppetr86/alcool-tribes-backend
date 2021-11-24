package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ColdWeaponEntity")
@Getter
@Setter
public class ColdWeaponEntity extends WeaponEntity {
    private Integer bladeLength;


    public ColdWeaponEntity(String name, WeaponType type, KingdomEntity kingdom, UUID id) {
        setName(name);
        setType(type);
        setKingdom(kingdom);
        setId(id);
    }

    public ColdWeaponEntity() {
    }

    public ColdWeaponEntity(UUID id, String name, WeaponType type, KingdomEntity kingdom, Integer bladeLength) {
        super(id, name, type, kingdom);
        this.bladeLength = bladeLength;
    }
}
