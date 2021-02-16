package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultBuildings;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuildingControllerIT2 {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BuildingService buildingService;

  @MockBean
  private ResourceService resourceService;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
  }

  @Test
  public void increaseTheGivenBuildingLevel404WithShouldReturnNoId() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(null);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, null)).thenReturn("no id");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn400WithParameterMissingAll() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity();
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("parameter missing");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): <type>!")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn400WithParameterMissingIdLevelHpFinishedAtKingdom() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(BuildingType.FARM, 300L);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("parameter missing");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): <type>!")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn400WithParameterMissingIdHpStartedAtFinishedAt() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("parameter missing");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): <type>!")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn400WithParameterMissingKingdom() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.MINE, 1, 100, 200L, 300L);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("parameter missing");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): <type>!")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn406withTownHallNeedHigherLevel() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("town hall need higher level");

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isNotAcceptable())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Invalid building level || Cannot build buildings with higher level than the Townhall")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn409WithNoResource() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(kingdom, BuildingType.FARM, 1);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("no resource");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isConflict())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn200WithBuildingDetails() throws Exception {
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    BuildingEntity building = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 200L, 260L, kingdom);
    BuildingEntity townHall = new BuildingEntity(kingdom, BuildingType.TOWNHALL, 2);
    List<BuildingEntity> fakeList = Arrays.asList(
      building,
      townHall
    );
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(building);
    Mockito.when(buildingService.increaseTheGivenBuildingLevel(kingdom, building)).thenReturn("building details");
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    building.setLevel(2);
    building.setStartedAt(300L);
    building.setFinishedAt(360L);
    Mockito.when(buildingService.updateBuilding(building)).thenReturn(building);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.type", is("FARM")))
      .andExpect(jsonPath("$.level", is(2)))
      .andExpect(jsonPath("$.hp", is(100)))
      .andExpect(jsonPath("$.startedAt", is(300)))
      .andExpect(jsonPath("$.finishedAt", is(360)));
  }
}
