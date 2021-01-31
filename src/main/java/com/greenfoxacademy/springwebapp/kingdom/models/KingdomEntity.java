package com.greenfoxacademy.springwebapp.kingdom.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Table
@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class KingdomEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany()
  @JoinColumn(name = "fk_buildings_kingdom")
  private List<BuildingEntity> building;

  @Column(name = "kingdomname")
  private String kingdomName;

  //TODO: need to add rest of fields
}
