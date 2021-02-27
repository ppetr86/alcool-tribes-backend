package com.greenfoxacademy.springwebapp.resource.models.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ResourceListResponseDTO {
  private List<ResourceResponseDTO> resources;

  public ResourceListResponseDTO(List<ResourceResponseDTO> resources) {
    this.resources = resources;
  }
}
