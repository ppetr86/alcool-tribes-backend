package com.greenfoxacademy.springwebapp.resource.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResourceListResponseDTO {
  private List<ResourceResponseDTO> resources;

  public ResourceListResponseDTO(List<ResourceResponseDTO> resources) {
    this.resources = resources;
  }
}
