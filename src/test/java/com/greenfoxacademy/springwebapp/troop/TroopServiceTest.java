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

  private final ModelMapper modelMapper = new ModelMapper();
  private TroopService troopService;
  private TroopEntityRepository troopEntityRepository;

  @Before
  public void init() {
    troopEntityRepository = Mockito.mock(TroopEntityRepository.class);
    troopService = new TroopServiceImpl(troopEntityRepository, modelMapper);
  }

  @Test
  public void findTroopsByKingdomID_ReturnsCorrectSet() {
    Set<TroopEntity> entities = new HashSet<>();
    entities.add(new TroopEntity(1L, 1, 100, 50, 20, 999, 1111));
    entities.add(new TroopEntity(1L, 1, 100, 50, 20, 1111, 1222));
    Mockito.when(troopEntityRepository.findAllByKingdomID(1L)).thenReturn(entities);
    Assert.assertEquals(2, entities.size());
  }

  @Test
  public void convertDTOSetToDTO_ReturnsCorrectResult() {
    TroopEntityResponseDto first = new TroopEntityResponseDto(1, 101, 101, 101, 101, 101);
    TroopEntityResponseDto second = new TroopEntityResponseDto(2, 102, 102, 102, 102, 102);
    TroopEntityResponseDto third = new TroopEntityResponseDto(3, 103, 103, 103, 103, 103);

    Set<TroopEntityResponseDto> input = new HashSet<>();
    input.add(first);
    input.add(second);
    input.add(third);

    TroopResponseDto output = new TroopResponseDto();
    output.setTroops(input);

    TroopResponseDto result = troopService.convertDTOSetToDTO(input);

    Assert.assertEquals(output.getTroops().size(), result.getTroops().size());
    Assert.assertEquals(output.getTroops().contains(first), result.getTroops().contains(first));
  }

  @Test
  public void convertEntityToEntityResponseDTO_inputDataMatchesOutput() {
    TroopEntity input = new TroopEntity(1L, 1, 100, 50, 20, 999, 1111);

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

  @Test
  public void convertEntitySetToDTO_inputDataMatchesOutput() {
    Set<TroopEntity> input = new HashSet<>();
    TroopEntity one = new TroopEntity(1L, 1, 101, 101, 101, 101, 101);
    TroopEntity two = new TroopEntity(2L, 2, 102, 102, 102, 102, 102);
    TroopEntity three = new TroopEntity(3L, 3, 103, 103, 103, 103, 103);
    input.add(one);
    input.add(two);
    input.add(three);

    TroopEntityResponseDto first = new TroopEntityResponseDto(1, 101, 101, 101, 101, 101);
    TroopEntityResponseDto second = new TroopEntityResponseDto(2, 102, 102, 102, 102, 102);
    TroopEntityResponseDto third = new TroopEntityResponseDto(3, 103, 103, 103, 103, 103);

    Set<TroopEntityResponseDto> output = new HashSet<>();
    output.add(first);
    output.add(second);
    output.add(third);

    TroopEntityResponseDto oneConverted = troopService.convertEntityToEntityResponseDTO(one);

    Set<TroopEntityResponseDto> result = troopService.convertEntitySetToDTO(input);

    Assert.assertEquals(output.size(), result.size());
    Assert.assertEquals(output.size(), result.size());
    Assert.assertFalse(result.isEmpty());
    Assert.assertTrue(result.contains(oneConverted));
    Assert.assertEquals(1, result.stream().filter(each -> each.getAttack() > 102).count());
    Assert.assertEquals(0, result.stream().filter(each -> each.getAttack() > 103).count());

  }


}