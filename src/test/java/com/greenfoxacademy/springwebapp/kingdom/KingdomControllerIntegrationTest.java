package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.kingdom.controllers.KingdomController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class KingdomControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

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
}