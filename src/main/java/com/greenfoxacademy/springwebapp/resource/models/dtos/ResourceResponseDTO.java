package com.greenfoxacademy.springwebapp.resource.models.dtos;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;

import java.util.List;

public class ResourceResponseDTO {
  private List<ResourceEntity> resources;

  public ResourceResponseDTO(List<ResourceEntity> resources) {
    this.resources = resources;
  }
}
