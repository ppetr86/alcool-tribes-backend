package com.greenfoxacademy.springwebapp.kingdom.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.factories.ResourceFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
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

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KingdomControllerIT {

  @Autowired
  private MockMvc mockMvc;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setResources(ResourceFactory.createResourcesWithAllData(null));
    kingdom.setKingdomName("Test Name");
  }

  @Test
  public void getKingdomID_returns200_andCorrectResultsWhenExistingKingdom() throws Exception {

    mockMvc.perform(get(KingdomController.URI+ "/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(1)))
            .andExpect(jsonPath("$.name", is("furkesz's kingdom")))
            .andExpect(jsonPath("$.userId", is(1)))
            .andExpect(jsonPath("$.buildings.[0].type", is("townhall")))
            .andExpect(jsonPath("$.resources.[0].type", is("food")))
            .andExpect(jsonPath("$.troops.[0].level", is(1)))
            .andExpect(jsonPath("$.location.x", is(10)));
  }

  @Test
  public void getKingdomID_returns404WhenNotExistingKingdom() throws Exception {
    mockMvc.perform(get("/kingdom/{id}", 1111))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void getKingdomResourcesShouldReturnCorrectAmount() throws Exception {
    mockMvc.perform(get(KingdomController.URI + "/resources")
        .principal(authentication))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.resources[0].amount", is(100)));
  }

  @Test
  public void updateKingdomWithNameShouldReturnUpdatedKingdom() throws Exception{
    KingdomNameDTO request = new KingdomNameDTO("New Kingdom");
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(request);

    mockMvc.perform(put(KingdomController.URI)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        .principal(authentication))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.kingdomName", is("New Kingdom")));
  }
}