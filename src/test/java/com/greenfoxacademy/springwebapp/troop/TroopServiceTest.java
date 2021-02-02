package com.greenfoxacademy.springwebapp.troop;

import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopResponseDto;
import com.greenfoxacademy.springwebapp.troop.repositories.TroopEntityRepository;
import com.greenfoxacademy.springwebapp.troop.services.TroopService;
import com.greenfoxacademy.springwebapp.troop.services.TroopServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Set;

public class TroopServiceTest {

  private TroopService troopService;
  private TroopEntityRepository troopEntityRepository;
  private ModelMapper modelMapper;

  @Before
  public void init(){
    troopEntityRepository = Mockito.mock(TroopEntityRepository.class);
    troopService = Mockito.mock(TroopService.class);
    troopService = new TroopServiceImpl(troopEntityRepository, modelMapper);
  }

  @Test
  public void findTroopsByKingdomID_ReturnsCorrectSet(){
    Set<TroopEntity> entities = new HashSet<>();
    entities.add(new TroopEntity(1L,1, 100,50,20,999,1111));
    entities.add(new TroopEntity(1L,1, 100,50,20,1111,1222));
    Mockito.when(troopEntityRepository.findAllByKingdomID(1L)).thenReturn(entities);
    Assert.assertEquals(2, entities.size());
  }

  @Test
  public void convertEntityToEntityResponseDTO_inputDataMatchesOutput(){
    TroopEntity input = new TroopEntity(1L,1, 100,50,20,999,1111);
    TroopEntityResponseDto output = new TroopEntityResponseDto();
    output.setLevel(input.getLevel());
    output.setHp(input.getHp());
    output.setAttack(input.getAttack());
    output.setDefence(input.getDefence());
    output.setStartedAt(input.getStartedAt());
    output.setFinishedAt(input.getFinishedAt());

    TroopEntityResponseDto result = troopService.convertEntityToEntityResponseDTO(input);

    Assert.assertEquals(output.getLevel(), result.getLevel());
    Assert.assertEquals(output.getHp(), result.getHp());
    Assert.assertEquals(output.getAttack(), result.getAttack());
    Assert.assertEquals(output.getDefence(), result.getDefence());
    Assert.assertEquals(output.getStartedAt(), result.getStartedAt());
    Assert.assertEquals(output.getFinishedAt(), result.getFinishedAt());
  }






}