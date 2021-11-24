package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeaponsDTO implements Serializable {

    private String name;
    private WeaponType type;
    private Integer bladeLength;
    private Integer magazineSize;
    private Boolean isConcealable;
    private Boolean isAutomaticable;
    private KingdomEntity kingdom;

    public WeaponsDTO() {
    }

    public WeaponsDTO(String name, WeaponType type, Integer bladeLength, Integer magazineSize, Boolean isConcealable, Boolean isAutomaticable, KingdomEntity kingdom) {
        this.name = name;
        this.type = type;
        this.bladeLength = bladeLength;
        this.magazineSize = magazineSize;
        this.isConcealable = isConcealable;
        this.isAutomaticable = isAutomaticable;
        this.kingdom = kingdom;
    }
}
