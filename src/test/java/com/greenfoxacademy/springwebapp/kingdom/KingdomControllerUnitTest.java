package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.kingdom.controllers.KingdomController;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class KingdomControllerUnitTest {

  private KingdomService kingdomService;
  private KingdomController kingdomController;

  @Before
  public void setUp(){
    kingdomService = Mockito.mock(KingdomService.class);
    kingdomController = new KingdomController(kingdomService);
  }

  @Test
  public void existingKingdomReturns200Status(){
    PlayerEntity player = new PlayerEntity();
    player.setId(1L);

    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("TEST_KINGDOM");
    kingdom.setId(1L);
    kingdom.setPlayer(player);

    KingdomResponseDTO dto = new KingdomResponseDTO(kingdom);

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(kingdomService.entityToKingdomResponseDTO(kingdom)).thenReturn(dto);
    ResponseEntity<Object> response = kingdomController.getKingdomByID(1L);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void non_existingKingdomReturns400_AndRelevantReponse(){
    Mockito.when(kingdomService.findByID(1L)).thenReturn(null);
    ResponseEntity<Object> response = kingdomController.getKingdomByID(1L);
    Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO)response.getBody()).getMessage());
  }
}