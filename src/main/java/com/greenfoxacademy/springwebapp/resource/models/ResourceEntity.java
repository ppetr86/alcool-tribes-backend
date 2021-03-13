package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources")
public class ResourceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private ResourceType type;
  private Integer amount;
  private Integer generation;
  private Long updatedAt;
  @ManyToOne
  private KingdomEntity kingdom;

  public ResourceEntity(KingdomEntity kingdomEntity, ResourceType type, Integer amount, Integer generation,
                        Long updatedAt) {
    this.kingdom = kingdomEntity;
    this.type = type;
    this.amount = amount;
    this.generation = generation;
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "ResourceEntity{" +
        "id=" + id +
        ", type=" + type +
        ", amount=" + amount +
        ", generation=" + generation +
        ", updatedAt=" + updatedAt;
  }
}