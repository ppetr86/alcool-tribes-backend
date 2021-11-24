package com.greenfoxacademy.springwebapp.weapon.models;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "AutomaticGunEntity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AutomaticGunEntity extends WeaponEntity {
    private Integer magazineSize;
    private Boolean isSemiautoable;
}
