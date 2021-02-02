package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.controllers.TroopController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TroopControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void getTroopsOfKingdom_ReturnsCorrectBodyAndStatus() throws Exception {
    mockMvc.perform(get(TroopController.URI_GET_KINGDOM_TROOPS))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }
}