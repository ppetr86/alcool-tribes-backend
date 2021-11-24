package com.greenfoxacademy.springwebapp.troop.models.dtos;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TroopEntityResponseDTO {

    private Long id;
    private int level;
    private int hp;
    private int attack;
    private int defence;
    private long startedAt;
    private long finishedAt;

    public TroopEntityResponseDTO(long id, int level, int hp, int attack, int defence, long startedAt,
                                  long finishedAt) {
        this.id = id;
        this.level = level;
        this.hp = hp;
        this.attack = attack;
        this.defence = defence;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    public TroopEntityResponseDTO(TroopEntity entity) {

        this.id = entity.getId();
        this.level = entity.getLevel();
        this.hp = entity.getHp();
        this.attack = entity.getAttack();
        this.defence = entity.getDefence();
        this.startedAt = entity.getStartedAt();
        this.finishedAt = entity.getFinishedAt();
    }
}