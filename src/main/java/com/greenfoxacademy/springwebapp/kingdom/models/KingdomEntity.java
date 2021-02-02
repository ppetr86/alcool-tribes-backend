package com.greenfoxacademy.springwebapp.kingdom.models;


import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

//TODO: ALTB-14-Petr renamed the table
@Table(name = "kingdoms")
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany()
  @JoinColumn(name = "fk_kingdom_id")
  private Set<BuildingEntity> buildings;

  @OneToMany
  @JoinColumn(name = "fk_kingdom_id")
  private Set<TroopEntity> troops;

  @Column(name = "kingdomname")
  private String kingdomName;

  //TODO: ALTB-14-Petr - I redefined the mapping. Please reconsider
  @OneToOne
  @JoinColumn(name = "fk_player_id")
  private PlayerEntity userId;

}