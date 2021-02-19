package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultBuildings;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BuildingControllerIT2 {

  @Autowired
  private MockMvc mockMvc;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
  }

  @Test
  public void getBuildingByIdShouldReturnOk() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.level", is(1)))
      .andExpect(jsonPath("$.hp", is(0)))
      .andExpect(jsonPath("$.startedAt", is(0)))
      .andExpect(jsonPath("$.finishedAt", is(0)))
      .andExpect(jsonPath("$.type", is("townhall")));
  }

  @Test
  public void getBuildingByIdShouldReturn404() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/16")
      .principal(authentication))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void getBuildingByIdShouldReturn403() throws Exception {
    mockMvc.perform(get(BuildingController.URI + "/5")
      .principal(authentication))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.message", is("Forbidden action")));
  }
}
