package com.greenfoxacademy.springwebapp.location.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntitySpecificationDto;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public interface LocationService {

    LocationEntity assignKingdomLocation(KingdomEntity kingdom);


    long countOfLocationsWhereXIsBiggerThan(int x);


    long countOfLocationsWhereXIsBiggerThanAndTypeIs(int x, LocationType type);


    Integer distanceBetweenKingdomsWithoutObstacles(KingdomEntity attackingKingdom, KingdomEntity defendingKingdom);


    boolean existsLocationWhereKingdomIsNotNull();


    List<LocationEntity> findLocationWhereXIsBiggerThan(int x);


    List<LocationEntity> findLocationWhereXIsBiggerThanAndTypeIs(int x, LocationType type);


    List<LocationEntity> findShortestPath(KingdomEntity start, KingdomEntity end);


    boolean isTypeChangeableToTarget(LocationEntity first, Set<LocationEntity> kingdoms);


    LocationEntity save(LocationEntity entity);


    LocationEntitySpecificationDto showMatchesBySpecification(int x, LocationType type);


    LocationEntityDTO[] showRandomMatchesByClassFieldsSpecifications(String... fields) throws WrongContentTypeException;
}
