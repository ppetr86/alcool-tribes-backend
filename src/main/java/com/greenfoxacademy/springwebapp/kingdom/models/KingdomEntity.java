package com.greenfoxacademy.springwebapp.kingdom.models;

import javax.persistence.*;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
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
}
