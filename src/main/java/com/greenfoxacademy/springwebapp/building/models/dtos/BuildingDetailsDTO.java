package com.greenfoxacademy.springwebapp.building.models.dtos;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingDetailsDTO  {

    //private static final long serialVersionUID = 2429967723233145524L;

    private long id;
    private String type;
    private int level;
    private int hp;
    private long startedAt;
    private long finishedAt;

    public BuildingDetailsDTO(BuildingEntity building) {
        this.id = building.getId();
        this.type = building.getType().toString().toLowerCase();
        this.level = building.getLevel();
        this.hp = building.getHp();
        this.startedAt = building.getStartedAt();
        this.finishedAt = building.getFinishedAt();
    }

    public BuildingDetailsDTO() {
    }

    public BuildingDetailsDTO(long id, String type, int level, int hp, long startedAt, long finishedAt) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.hp = hp;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

}
