package com.greenfoxacademy.springwebapp.building.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingLevelDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingRequestDTO;
import com.greenfoxacademy.springwebapp.factories.AuthFactory;
import com.greenfoxacademy.springwebapp.factories.BuildingFactory;
import com.greenfoxacademy.springwebapp.factories.PlayerFactory;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth2;
import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuthWithResources;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultBuildings;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuildingControllerIT {

  @Autowired
  private MockMvc mockMvc;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(BuildingFactory.createBuildingsWhereTownHallsLevelFive());
    kingdom.setResources(ResourceFactory.createResourcesWithAllDataWithLowAmount());
    kingdom.setBuildings(createDefaultBuildings(kingdom));
    kingdom.setPlayer(PlayerFactory.createPlayer(1L, kingdom));
  }

  @Test
  public void getKingdomBuildings() throws Exception {
    mockMvc.perform(get(BuildingController.URI)
        .principal(authentication))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.buildings[0].finishedAt", is(200)));
  }

  @Test
  public void buildBuilding_BuildingCreated() throws Exception {
    Authentication auth = createAuthWithResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    BuildingRequestDTO request = new BuildingRequestDTO("farm");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(auth))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.type", is("FARM")));
  }

  @Test
  public void buildBuilding_EmptyInput() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(authentication))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_EmptyInput_NoTypeProvidedReturnsMissingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO();
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(authentication))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Missing parameter(s): type!")));
  }

  @Test
  public void buildBuilding_EmptyInput_WhitespaceInputReturnsInvalidBuildingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("  ");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(post(BuildingController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(authentication))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Invalid building type")));

  }

  @Test
  public void buildBuilding_WrongBuildingType() throws Exception {
    BuildingRequestDTO request = new BuildingRequestDTO("WRONG_TYPE");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

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
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(new ArrayList<>());

    mockMvc.perform(post(BuildingController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(authentication))
        .andExpect(status().isNotAcceptable())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }

  @Test
  public void getBuildingByIdShouldReturnOkAndProperBuilding() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/1")
        .principal(authentication))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.level", is(1)))
        .andExpect(jsonPath("$.hp", is(100)))
        .andExpect(jsonPath("$.startedAt", is(100)))
        .andExpect(jsonPath("$.finishedAt", is(200)))
        .andExpect(jsonPath("$.type", is("townhall")));
  }

  @Test
  public void getBuildingByIdShouldReturn404WhenNonExistingBuildingIdGiven() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/16")
        .principal(authentication))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void getBuildingByIdShouldReturn403WhenNotOwnBuildingRequested() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/5")
        .principal(authentication))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message", is("Forbidden action")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnNotFoundWithNoIdMessage() throws Exception {
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
  public void updateTheGivenBuildingDetailsShouldReturnBadRequestWithKingdomParameterMissing() throws Exception {
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
  public void updateTheGivenBuildingDetailsShouldReturnNotAcceptablewithTownHallNeedHigherLevel() throws Exception {
    Authentication auth2 = createAuth2(1L);

    BuildingLevelDTO request = new BuildingLevelDTO(2);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(BuildingController.URI + "/2")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(auth2))
      .andExpect(status().isNotAcceptable())
      .andExpect(jsonPath("$.status", is("error")))
      .andExpect(jsonPath("$.message", is("Cannot build buildings with higher level than the Townhall")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnConflictWithNoResource() throws Exception {
    Authentication auth = AuthFactory.createAuth2(3L);
    BuildingLevelDTO request = new BuildingLevelDTO(2);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(BuildingController.URI + "/9")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(auth))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not enough resource")));
  }

  @Test
  public void updateTheGivenBuildingDetailsShouldReturnOkWithBuildingDetails() throws Exception {
    Authentication auth = createAuthWithResources(ResourceFactory.createResourcesWithAllDataWithHighAmount());
    BuildingLevelDTO request = new BuildingLevelDTO(3);
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(BuildingController.URI + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content(json)
      .principal(auth))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id", is(1)))
      .andExpect(jsonPath("$.type", is("TOWNHALL")))
      .andExpect(jsonPath("$.level", is(3)))
      .andExpect(jsonPath("$.hp", is(600)));
  }
}