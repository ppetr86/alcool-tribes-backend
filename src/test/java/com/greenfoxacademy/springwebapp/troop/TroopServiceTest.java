package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopEntityRepository;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import com.greenfoxacademy.springwebapp.troop.services.TroopServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class TroopServiceTest {

  private final ModelMapper modelMapper = new ModelMapper();
  private TroopService troopService;
  private TroopEntityRepository troopEntityRepository;

  List<TroopEntity> inputTroopEntities = new ArrayList<>();
  List<TroopEntityResponseDTO> inputTroopEntityResponseDTO = new ArrayList<>();
  TroopEntity troopEntity1;
  TroopEntity troopEntity2;
  TroopEntity troopEntity3;
  TroopEntityResponseDTO entityResponseDTO1;
  TroopEntityResponseDTO entityResponseDTO2 ;
  TroopEntityResponseDTO entityResponseDTO3;

  @Before
  public void init() {
    troopEntityRepository = Mockito.mock(TroopEntityRepository.class);
    troopService = new TroopServiceImpl(troopEntityRepository, modelMapper);

    troopEntity1 = new TroopEntity(1L, 1, 101, 101, 101, 101L, 101L);
    troopEntity2 = new TroopEntity(2L, 2, 102, 102, 102, 102L, 102L);
    troopEntity3 = new TroopEntity(3L, 3, 103, 103, 103, 103L, 103L);
    inputTroopEntities.add(troopEntity1);
    inputTroopEntities.add(troopEntity2);
    inputTroopEntities.add(troopEntity3);

    entityResponseDTO1 = new TroopEntityResponseDTO(1, 101, 101, 101, 101, 101);
    entityResponseDTO2 = new TroopEntityResponseDTO(2, 102, 102, 102, 102, 102);
    entityResponseDTO3 = new TroopEntityResponseDTO(3, 103, 103, 103, 103, 103);

    inputTroopEntityResponseDTO.add(entityResponseDTO1);
    inputTroopEntityResponseDTO.add(entityResponseDTO2);
    inputTroopEntityResponseDTO.add(entityResponseDTO3);
  }

  @Test
  public void findTroopsByKingdomID_ReturnsCorrectList() {
    Mockito.when(troopEntityRepository.findAllByKingdom(new KingdomEntity(1L))).thenReturn(inputTroopEntities);
    Assert.assertEquals(3, inputTroopEntities.size());
  }

  @Test
  public void convertDTOListToDTO_ReturnsCorrectResult() {
    TroopResponseDto output = new TroopResponseDto();
    output.setTroops(inputTroopEntityResponseDTO);

    TroopResponseDto result = troopService.convertDTOListToDTO(inputTroopEntityResponseDTO);

    Assert.assertEquals(output.getTroops().size(), result.getTroops().size());
    Assert.assertEquals(output.getTroops().contains(entityResponseDTO3), result.getTroops().contains(entityResponseDTO3));
  }

  @Test
  public void convertEntityToEntityResponseDTO_inputDataMatchesOutput() {
    TroopEntityResponseDTO output = new TroopEntityResponseDTO();
    output.setLevel(troopEntity1.getLevel());
    output.setHp(troopEntity1.getHp());
    output.setAttack(troopEntity1.getAttack());
    output.setDefence(troopEntity1.getDefence());
    output.setStartedAt(troopEntity1.getStartedAt());
    output.setFinishedAt(troopEntity1.getFinishedAt());

    TroopEntityResponseDTO result = troopService.convertEntityToEntityResponseDTO(troopEntity1);

    Assert.assertEquals(output.getLevel(), result.getLevel());
    Assert.assertEquals(output.getHp(), result.getHp());
    Assert.assertEquals(output.getAttack(), result.getAttack());
    Assert.assertEquals(output.getDefence(), result.getDefence());
    Assert.assertEquals(output.getStartedAt(), result.getStartedAt());
    Assert.assertEquals(output.getFinishedAt(), result.getFinishedAt());
  }

  @Test
  public void convertEntityListToDTO_inputDataMatchesOutput() {
    TroopEntityResponseDTO oneConverted = troopService.convertEntityToEntityResponseDTO(troopEntity1);

    List<TroopEntityResponseDTO> result = troopService.convertEntityListToDTO(inputTroopEntities);

    Assert.assertEquals(inputTroopEntityResponseDTO.size(), result.size());
    Assert.assertEquals(inputTroopEntityResponseDTO.size(), result.size());
    Assert.assertFalse(result.isEmpty());
    Assert.assertTrue(result.contains(oneConverted));
    Assert.assertEquals(1, result.stream().filter(each -> each.getAttack() > 102).count());
    Assert.assertEquals(0, result.stream().filter(each -> each.getAttack() > 103).count());
  }
}