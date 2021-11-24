package com.greenfoxacademy.springwebapp.weapon.models;

import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingBean {

    private BuildingType buildingType;
    private Long buildingId;
}
