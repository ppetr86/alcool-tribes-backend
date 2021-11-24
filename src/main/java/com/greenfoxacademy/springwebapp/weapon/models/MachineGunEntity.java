package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.util.UUID;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "MachineGunEntity")
@Getter
@Setter
public class MachineGunEntity extends WeaponEntity {
    private Integer magazineSize;

    public MachineGunEntity(String name, WeaponType type, KingdomEntity kingdom, UUID id) {
        setName(name);
        setType(type);
        setKingdom(kingdom);
        setId(id);
    }

    public MachineGunEntity(UUID id, String name, WeaponType type, KingdomEntity kingdom, Integer magazineSize) {
        super(id, name, type, kingdom);
        this.magazineSize = magazineSize;
    }

    public MachineGunEntity() {
    }
}
