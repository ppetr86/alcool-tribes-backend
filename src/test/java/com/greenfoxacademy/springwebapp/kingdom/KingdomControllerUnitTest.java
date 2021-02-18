package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
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

    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("testKingdom");
    kingdom.setId(1L);

    PlayerEntity pl = new PlayerEntity(1L, "testUser", "password", "test@test.com",null,null,kingdom );
    kingdom.setPlayer(pl);

    KingdomResponseDTO result = kingdomService.entityToKingdomResponseDTO(1L);

    Mockito.when(kingdomService.findByID(1L)).thenReturn(kingdom);
    Mockito.when(kingdomService.entityToKingdomResponseDTO(1L)).thenReturn(result);
  }

  @Test
  public void existingKingdomReturns200Status(){

    ResponseEntity<Object> response = kingdomController.getKingdomByID(1L);
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test(expected = IdNotFoundException.class)
  public void non_existingKingdomReturns400_AndRelevantReponse(){
    Mockito.when(kingdomService.entityToKingdomResponseDTO(1111L)).thenThrow(IdNotFoundException.class);

    ResponseEntity<Object> response = kingdomController.getKingdomByID(1111L);
    Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    Assert.assertEquals("Id not found", ((ErrorDTO)response.getBody()).getMessage());
  }
}