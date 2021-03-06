package com.greenfoxacademy.springwebapp.troop.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "troops")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TroopEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  private Long startedAt;
  private Long finishedAt;
  @ManyToOne
  private KingdomEntity kingdom;
  private boolean isHome;

  public TroopEntity(Long id, int level, int hp, int attack,
                     int defence, long startedAt, long finishedAt) {
    this.id = id;
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defence;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
  }

  @Override
  public String toString() {
    return "TroopEntity"
        + "id=" + id
        + ", level=" + level
        + ", hp=" + hp
        + ", attack=" + attack
        + ", defence=" + defence
        + ", startedAt=" + startedAt
        + ", finishedAt=" + finishedAt
        + ", kingdom name=" + kingdom.getKingdomName();
  }
}