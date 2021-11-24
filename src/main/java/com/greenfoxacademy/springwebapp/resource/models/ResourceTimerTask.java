package com.greenfoxacademy.springwebapp.resource.models;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceServiceImpl;
import java.util.TimerTask;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResourceTimerTask extends TimerTask {
    private ResourceEntity resource;
    private Integer generation;
    private BuildingEntity building;
    private ResourceServiceImpl resourceService;

    @Override
    public void run() {
        resourceService.scheduledResourceUpdate(this.getResource(), this.getGeneration(),
                this.getBuilding());
    }
}
