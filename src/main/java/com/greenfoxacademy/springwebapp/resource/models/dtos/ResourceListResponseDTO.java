package com.greenfoxacademy.springwebapp.resource.models.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ResourceListResponseDTO {
    private List<ResourceResponseDTO> resources;

    public ResourceListResponseDTO(List<ResourceResponseDTO> resources) {
        this.resources = resources;
    }
}
