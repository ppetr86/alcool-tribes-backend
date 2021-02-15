package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "resources")
public class ResourceEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private ResourceType type;
  private int amount;
  private int generation;
  private long updatedAt;
  @ManyToOne
  private KingdomEntity kingdom;

  public ResourceEntity(ResourceType type, int amount, int generation, long updatedAt) {
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.updatedAt = updatedAt;
  }
}
