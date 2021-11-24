package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "SniperRifleEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SniperRifleEntity extends WeaponEntity {
    private Integer magazineSize;
    private Integer shootingDistance;

    public SniperRifleEntity(String name, WeaponType type, KingdomEntity kingdom, UUID id) {
        setName(name);
        setType(type);
        setKingdom(kingdom);
        setId(id);
    }
}
