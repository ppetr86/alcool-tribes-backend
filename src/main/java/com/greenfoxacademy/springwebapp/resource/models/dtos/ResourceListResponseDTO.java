package com.greenfoxacademy.springwebapp.resource.models.dtos;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;

import java.util.List;

public class ResourceListResponseDTO {
  private List<ResourceEntity> resources;

  public ResourceListResponseDTO(List<ResourceEntity> resources) {
    this.resources = resources;
  }
}
