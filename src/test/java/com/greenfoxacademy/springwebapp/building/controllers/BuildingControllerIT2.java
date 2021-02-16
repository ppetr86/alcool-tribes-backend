package com.greenfoxacademy.springwebapp.building.controllers;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.building.services.BuildingService;
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

  @MockBean
  private BuildingService buildingService;

  private Authentication authentication;

  @Before
  public void setUp() throws Exception {
    authentication = createAuth("Furkesz", 1L);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    kingdom.setBuildings(createDefaultBuildings());
  }

  @Test
  public void getBuildingByIdShouldReturnOk() throws Exception{
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 200, 300);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildingEntity);
    Mockito.when(buildingService.kingdomIsContainTheGivenBuilding(kingdom,buildingEntity)).thenReturn(true);

    mockMvc.perform(get(BuildingController.URI + "/1")
      .principal(authentication))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.level", is(1)))
      .andExpect(jsonPath("$.hp", is(100)))
      .andExpect(jsonPath("$.startedAt", is(200)))
      .andExpect(jsonPath("$.finishedAt", is(300)));
  }

  @Test
  public void getBuildingByIdShouldReturn404() throws Exception{
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 200, 300);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(1L)).thenReturn(buildingEntity);
    //Is this need? Because the test, does not reach this point.
    Mockito.when(buildingService.kingdomIsContainTheGivenBuilding(kingdom,buildingEntity)).thenReturn(false);

    mockMvc.perform(get(BuildingController.URI + "/2")
      .principal(authentication))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message", is("Id not found")));
  }

  @Test
  public void getBuildingByIdShouldReturn403() throws Exception{
    BuildingEntity buildingEntity = new BuildingEntity(1L, BuildingType.FARM, 1, 100, 200, 300);
    BuildingEntity buildingEntity2 = new BuildingEntity(2L, BuildingType.FARM, 1, 100, 200, 300);
    KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
    List<BuildingEntity> fakeList = new ArrayList<>();
    fakeList.add(buildingEntity);
    kingdom.setBuildings(fakeList);

    Mockito.when(buildingService.findBuildingById(2L)).thenReturn(buildingEntity2);
    Mockito.when(buildingService.kingdomIsContainTheGivenBuilding(kingdom,buildingEntity2)).thenReturn(false);

    mockMvc.perform(get(BuildingController.URI + "/2")
      .principal(authentication))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.message", is("Forbidden action")));
  }
}
