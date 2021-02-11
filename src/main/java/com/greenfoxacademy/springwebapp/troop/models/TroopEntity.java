package com.greenfoxacademy.springwebapp.troop.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "troops")
public class TroopEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(updatable = false)
  private Long id;
  private Integer level;
  private Integer hp;
  private Integer attack;
  private Integer defence;
  @Column(updatable = false)
  private Long startedAt;
  @Column(updatable = false)
  private Long finishedAt;
  @ManyToOne
  private KingdomEntity kingdomEntity;

  public TroopEntity(Integer level, Integer hp, Integer attack, Integer defense, Long startedAt, Long finishedAt,
                     KingdomEntity kingdomEntity) {
    this.level = level;
    this.hp = hp;
    this.attack = attack;
    this.defence = defense;
    this.startedAt = startedAt;
    this.finishedAt = finishedAt;
    this.kingdomEntity = kingdomEntity;
  }


}
