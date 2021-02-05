package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.buildings.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Table
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  private PlayerEntity player;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "kingdom", fetch = FetchType.EAGER)
  private Set<BuildingEntity> buildings;

  @Column(name = "kingdomname")
  private String kingdomName;

}
