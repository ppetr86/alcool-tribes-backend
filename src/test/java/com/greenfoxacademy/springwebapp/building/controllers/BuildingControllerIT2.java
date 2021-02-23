package com.greenfoxacademy.springwebapp.building.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createBuildings;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(createBuildings(kingdom));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturn404WithNoId() throws Exception {
    BuildingLevelDTO request = new BuildingLevelDTO();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(BuildingController.URI + "/1123")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(authentication))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturn400WithKingdomParameterMissing() throws Exception {
    BuildingLevelDTO request = new BuildingLevelDTO();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(BuildingController.URI + "/4")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(authentication))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Missing parameter(s): level!")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturn406withTownHallNeedHigherLevel() throws Exception {
    BuildingLevelDTO request = new BuildingLevelDTO(2);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    //TODO: have to remove after hasResourceForBuilding will modify
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    mockMvc.perform(put(BuildingController.URI + "/2")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(authentication))
      .andExpect(status().isNotAcceptable())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturn409WithNoResource() throws Exception {
    BuildingLevelDTO request = new BuildingLevelDTO(2);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    //TODO: have to remove after hasResourceForBuilding will modify
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(authentication))
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturn200WithBuildingDetails() throws Exception {
    BuildingLevelDTO request = new BuildingLevelDTO(3);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    //TODO: have to remove after hasResourceForBuilding will modify
    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(authentication))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.type", is("TOWNHALL")))
      .andExpect(jsonPath("$.level", is(3)))
      .andExpect(jsonPath("$.hp", is(600)));
  }
}
