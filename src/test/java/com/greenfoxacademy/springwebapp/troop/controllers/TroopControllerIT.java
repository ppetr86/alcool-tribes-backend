package com.greenfoxacademy.springwebapp.troop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.enums.BuildingType;
import com.greenfoxacademy.springwebapp.factories.TroopFactory;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;
import static com.greenfoxacademy.springwebapp.factories.BuildingFactory.createDefaultLevel1BuildingsWithAllData;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TroopControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        authentication = createAuth("Furkesz", 1L);
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        kingdom.setBuildings(createDefaultLevel1BuildingsWithAllData());
    }

    @Test
    public void createTroopWithCorrectBuildingId_level1Academy_createsLevel1troop()
            throws Exception {
        TroopRequestDTO request = new TroopRequestDTO(4L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.level", is(1)))
                .andExpect(jsonPath("$.hp", is(20)))
                .andExpect(jsonPath("$.attack", is(10)))
                .andExpect(jsonPath("$.defence", is(5)))
                .andExpect(jsonPath("$.startedAt").isNumber())
                .andExpect(jsonPath("$.finishedAt").isNumber());
    }

    @Test
    public void createTroopWithCorrectBuildingId_level10Academy_createsLevel10troop()
            throws Exception {
        TroopRequestDTO request = new TroopRequestDTO(1L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        //creation of Level10 academy and its injection into Kingdom
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        List<BuildingEntity> buildings = new ArrayList<>();
        BuildingEntity academy =
                new BuildingEntity(1L, BuildingType.ACADEMY, 10, 150, 1613303221L, 1613303371L);
        buildings.add(academy);
        kingdom.setBuildings(buildings);

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.level", is(10)))
                .andExpect(jsonPath("$.hp", is(200)))
                .andExpect(jsonPath("$.attack", is(100)))
                .andExpect(jsonPath("$.defence", is(50)))
                .andExpect(jsonPath("$.startedAt").isNumber())
                .andExpect(jsonPath("$.finishedAt").isNumber());
    }

    @Test
    public void createTroopWithIncorrectBuildingId_LessThan1ValueOfId_Returns400Error()
            throws Exception {
        TroopRequestDTO request = new TroopRequestDTO(0L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Building ID must be higher than 0!")));
    }

    @Test
    public void createTroopWithIncorrectBuildingId_NullId_Returns400Error()
            throws Exception {

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Building Id cannot be null!")));
    }

    @Test
    public void createTroopWithIncorrectBuildingId_IdNotBelongToAnyBuildingInKingdom_Returns403Error()
            throws Exception {
        TroopRequestDTO request = new TroopRequestDTO(10L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(403))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Forbidden action")));
    }

    @Test
    public void createTroopWithIncorrectBuildingId_IdBelongsToSomeBuildingInKingdomButIsNotAcademy_Returns406Error()
            throws Exception {
        TroopRequestDTO request = new TroopRequestDTO(2L);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        mockMvc.perform(post(TroopController.URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(406))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Not a valid academy id")));
    }

    @Test
    public void getTroopsOfKingdom_ReturnsCorrectBodyAndStatus() throws Exception {
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        kingdom.setTroops(TroopFactory.createDefaultTroops());

        mockMvc.perform(get(TroopController.URI)
                .principal(authentication))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.troops[0].level", is(1)))
                .andExpect(jsonPath("$.troops[0].hp", is(101)));
    }

    @Test
    public void returnTroop_ReturnsCorrectBodyAndStatus() throws Exception {
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        kingdom.setTroops(TroopFactory.createDefaultTroops());

        mockMvc.perform(get(TroopController.URI + "/2")
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.hp", is(102)));
    }

    @Test
    public void returnTroop_ReturnsForbiddenExceptionDTO() throws Exception {
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        kingdom.setTroops(TroopFactory.createDefaultTroops());

        mockMvc.perform(get(TroopController.URI + "/6")
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Forbidden action")));
    }

    @Test
    public void returnTroop_ReturnsIdNotFoundException() throws Exception {
        KingdomEntity kingdom = ((CustomUserDetails) authentication.getPrincipal()).getKingdom();
        kingdom.setTroops(TroopFactory.createDefaultTroops());

        mockMvc.perform(get(TroopController.URI + "/9999")
                .principal(authentication))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Id not found")));
    }
}