package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.resource.models.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//TODO:ALTB-14

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources")
public class ResourceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private ResourceType type;
  private int amount;
  private int generation;
  private long updatedAt;
}
