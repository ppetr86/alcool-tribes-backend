package com.greenfoxacademy.springwebapp.building.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestNoSecurityConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuildingControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private KingdomService kingdomService;

  @MockBean
  private ResourceService resourceService;


  public Authentication createAuth(String userName, Long kingdomId) {

    CustomUserDetails userDetails = new CustomUserDetails();
    PlayerEntity player = new PlayerEntity();
    player.setUsername(userName);
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setId(kingdomId);

    userDetails.setLogin(player);
    userDetails.setKingdom(kingdom);

    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  @Test
  public void getKingdomBuildings() throws Exception {
    Authentication authentication = createAuth("Furkesz", 1L);
    mockMvc.perform(get(BuildingController.URI)
            .principal(authentication))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.buildings[0].finishedAt", is(0)));
  }

  @Test
  public void buildBuilding_BuildingCreated() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    Authentication authentication = createAuth("Furkesz", 1L);

    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(true);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.type", is("FARM")));
  }

  @Test
  public void buildBuilding_EmptyInput() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    Authentication authentication = createAuth("Furkesz", 1L);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_EmptyInputV2_NoTypeProvidedReturnsMissingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    Authentication authentication = createAuth("Furkesz", 1L);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_EmptyInputV3_WhitespaceInputReturnsMissingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("  ");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    Authentication authentication = createAuth("Furkesz", 1L);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_WrongBuildingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("WRONG_TYPE");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    Authentication authentication = createAuth("Furkesz", 1L);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Invalid building type")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForFarm() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("farM");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    Authentication authentication = createAuth("Furkesz", 1L);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForTownhall() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("TOWNhall");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    Authentication authentication = createAuth("Furkesz", 1L);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForMine() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("MINE");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    Authentication authentication = createAuth("Furkesz", 1L);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void buildBuilding_NotEnoughtResourcesForAcademy() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("academy");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(false);

    Authentication authentication = createAuth("Furkesz", 1L);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(false);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(true);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isConflict())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  // TODO: solve the functionality behind this test
  public void buildBuilding_NoTownhallInKingdom() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("academy");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);
    // TODO: remove this when ResourceService is implemented
    //Mockito.when(mockResourceService.hasResourcesForBuilding()).thenReturn(true);

    Authentication authentication = createAuth("Furkesz", 1L);

    Mockito.when(resourceService.hasResourcesForBuilding()).thenReturn(true);
    Mockito.when(kingdomService.hasKingdomTownhall()).thenReturn(false);

    mockMvc.perform(post(BuildingController.URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            .principal(authentication))
            .andExpect(status().isNotAcceptable())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }
}