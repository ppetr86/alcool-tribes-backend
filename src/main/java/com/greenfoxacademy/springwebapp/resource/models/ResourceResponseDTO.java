package com.greenfoxacademy.springwebapp.resource.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponseDTO {

  private String type;
  private int amount;
  private int generation;
  private long updatedAt;

  public ResourceResponseDTO(ResourceEntity en) {
    this.type = en.getType().resourceType;
    this.amount = en.getAmount();
    this.generation = en.getGeneration();
    this.updatedAt = en.getUpdatedAt();
  }
}
