package com.greenfoxacademy.springwebapp.troop.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidAcademyIdException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MissingParameterException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.NotEnoughResourceException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.troop.models.TroopEntity;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopListResponseDto;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopRequestDTO;
import java.util.List;

public interface TroopService {

    TroopEntityResponseDTO createTroop(KingdomEntity kingdom, TroopRequestDTO requestDTO) throws
            ForbiddenActionException, InvalidAcademyIdException, NotEnoughResourceException;


    void deleteListOfTroops(List<TroopEntity> deadTroops);


    TroopEntity findTroopById(Long id);


    TroopEntityResponseDTO getTroop(KingdomEntity kingdom, Long troopId) throws
            ForbiddenActionException, IdNotFoundException;


    void saveAllTroops(List<TroopEntity> troops);


    TroopListResponseDto troopsToListDTO(KingdomEntity entity);


    TroopEntityResponseDTO updateTroopLevel(KingdomEntity kingdomEntity, TroopRequestDTO requestDTO, Long troopId) throws
            MissingParameterException, ForbiddenActionException, IdNotFoundException,
            InvalidInputException, NotEnoughResourceException;
}
