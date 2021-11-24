package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingListResponseDTO {

    private List<BuildingEntity> buildings;

    public BuildingListResponseDTO(List<BuildingEntity> buildings) {
        this.buildings = buildings;
    }
}
