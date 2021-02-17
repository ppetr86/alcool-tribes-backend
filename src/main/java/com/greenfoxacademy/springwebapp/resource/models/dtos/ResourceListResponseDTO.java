package com.greenfoxacademy.springwebapp.resource.models.dtos;

import java.util.List;

public class ResourceListResponseDTO {
  private List<ResourceResponseDTO> resources;

  public ResourceListResponseDTO(List<ResourceResponseDTO> resources) {
    this.resources = resources;
  }
}
