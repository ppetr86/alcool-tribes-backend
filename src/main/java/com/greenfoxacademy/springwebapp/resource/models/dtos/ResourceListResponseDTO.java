package com.greenfoxacademy.springwebapp.resource.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ResourceListResponseDTO {
  private List<ResourceResponseDTO> resources;

  public ResourceListResponseDTO(List<ResourceResponseDTO> resources) {
    this.resources = resources;
  }
}
