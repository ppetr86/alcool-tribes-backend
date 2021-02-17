package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "kingdoms")
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private PlayerEntity player;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom", fetch = FetchType.EAGER)
  private List<BuildingEntity> buildings;

  @Column(name = "kingdomname")
  private String kingdomName;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom")
  private List<TroopEntity> troops;

  public KingdomEntity(List<TroopEntity> troops) {
    this.troops = troops;
  }
}