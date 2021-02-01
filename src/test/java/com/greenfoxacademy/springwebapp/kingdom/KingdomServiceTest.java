package com.greenfoxacademy.springwebapp.kingdom;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomEntityRepository;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.kingdom.services.KingdomServiceImpl;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class KingdomServiceTest {
  private KingdomService kingdomService;
  private KingdomEntityRepository kingdomEntityRepository;

  @Before
  public void init() {
    kingdomEntityRepository = Mockito.mock(KingdomEntityRepository.class);
    kingdomService = new KingdomServiceImpl(kingdomEntityRepository);
  }

  @Test
  public void findByID_ReturnsCorrectKingdom(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("TEST_KINGDOM");
    kingdom.setId(2L);
    Mockito.when(kingdomEntityRepository.findById(2L)).thenReturn(java.util.Optional.of(kingdom));
    Assert.assertEquals(kingdom, kingdomService.findByID(2L));
    Assert.assertEquals("TEST_KINGDOM", kingdomService.findByID(2L).getKingdomName());
  }

  @Test
  public void kingdomResponseDTO_returnsCorrectResults(){
    KingdomEntity kingdom = new KingdomEntity();
    kingdom.setKingdomName("TEST_KINGDOM");
    kingdom.setId(2L);

  }


}




  /*PlayerEntity player = new PlayerEntity();
    player.setId(1L);

            KingdomEntity kingdom = new KingdomEntity();
            kingdom.setKingdomName("TEST_KINGDOM");
            kingdom.setId(1L);
            kingdom.setUserId(player);

            KingdomResponseDTO dto = new KingdomResponseDTO(kingdom);*/
