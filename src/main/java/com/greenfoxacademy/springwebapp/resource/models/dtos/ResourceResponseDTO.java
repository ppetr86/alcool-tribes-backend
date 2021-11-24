package com.greenfoxacademy.springwebapp.resource.models.dtos;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
