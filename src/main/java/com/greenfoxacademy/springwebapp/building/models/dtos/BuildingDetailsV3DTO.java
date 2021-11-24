package com.greenfoxacademy.springwebapp.building.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuildingDetailsV3DTO {

    private long id;
    private int level;
    private int hp;
    private long startedAt;
    private long finishedAt;
    private String kingdomName;

    public BuildingDetailsV3DTO(long id, int level, int hp, long startedAt, long finishedAt, String kingdomName) {
        this.id = id;
        this.level = level;
        this.hp = hp;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
        this.kingdomName = kingdomName;
    }
}
