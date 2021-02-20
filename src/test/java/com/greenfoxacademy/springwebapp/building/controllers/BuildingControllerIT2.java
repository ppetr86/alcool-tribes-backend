package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.TownhallLevelException;
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
  private ResourceService resourceService;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
  }

  @Test
  public void increaseTheGivenBuildingLevel404WithShouldReturnNoId() throws Exception {
    mockMvc.perform(put(BuildingController.URI + "/1123")
      .principal(authentication))
      .andExpect(status().isNotFound())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn400WithParameterMissingKingdom() throws Exception {
    mockMvc.perform(put(BuildingController.URI + "/6")
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn406withTownHallNeedHigherLevel() throws Exception {
    mockMvc.perform(put(BuildingController.URI + "/2")
      .principal(authentication))
      .andExpect(status().isNotAcceptable())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }

  @Test
  public void increaseTheGivenBuildingLevelShouldReturn409WithNoResource() throws Exception {
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
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.type", is("TOWNHALL")))
      .andExpect(jsonPath("$.level", is(2)))
      .andExpect(jsonPath("$.hp", is(100)))
      .andExpect(jsonPath("$.startedAt", is(300)))
      .andExpect(jsonPath("$.finishedAt", is(360)));
  }
}
