package com.greenfoxacademy.springwebapp.building.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(TestNoSecurityConfig.class)
public class BuildingsControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype());
  @MockBean
  private ResourceService mockResourceService;

  @Test
  public void buildBuilding_BuildingCreated() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(true);


    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.type", is("FARM")));
  }

  @Test
  public void buildBuilding_EmptyInput() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_EmptyInputV2() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_WrongBuildingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("WRONG_TYPE");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Invalid building type")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForFarm() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("farM");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForTownhall() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("TOWNhall");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForMine() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("MINE");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForAcademy() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("academy");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  // TODO: solve the functionality behind this test
  public void buildBuilding_NoTownhallInKingdom() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("academy");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(true);

    mockMvc.perform(post(BuildingsController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }
}
