package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "ShotGunEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShotGunEntity extends WeaponEntity {
    private Integer magazineSize;

    public ShotGunEntity(String name, WeaponType type, KingdomEntity kingdom, UUID id) {
    }
}
