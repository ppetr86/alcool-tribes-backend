package com.greenfoxacademy.springwebapp.location.repositories;

import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.location.models.enums.LocationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>, JpaSpecificationExecutor<LocationEntity> {

    long countAllByTypeIs(LocationType type);


    List<LocationEntity> findAll();


    @Query(value =
            "select * from locations where x>=:minX AND x<=:maxX AND y <= :maxY AND y >= :minY", nativeQuery = true)
    List<LocationEntity> findAllInRectangle(int minX, int maxX, int maxY, int minY);


    @Query(value =
            "select * from locations where x>=:minX AND x<=:maxX AND y <= :maxY AND y >= :minY ORDER BY y DESC, x ASC ",
            nativeQuery = true)
    List<LocationEntity> findAllInRectangleOrdered(int minX, int maxX, int maxY, int minY);


    List<LocationEntity> findAllLocationsByTypeIs(LocationType type);


    //@Query(value = "select * from locations where x=:x AND y=:y", nativeQuery = true)
    //LocationEntity findByXIsAndYIs(int x, int y);

    LocationEntity findByXIsAndYIs(int x, int y);


    @Procedure
    List<LocationEntity> generateEmptyLocations(int n);


    @Procedure
    List<LocationEntity> generateNDesertsAndJunglesRandSelect(int n);


    @Query(value = "select max(id) from locations", nativeQuery = true)
    Long getMaxID();

    boolean existsByKingdomIsNotNull();

    @Query(value = "select min(id) from locations", nativeQuery = true)
    long getMinID();


    @Query(value = "select max(x) from locations", nativeQuery = true)
    int xMax();

    @Query(value = "select min(x) from locations", nativeQuery = true)
    int xMin();

    @Query(value = "select max(y) from locations", nativeQuery = true)
    int yMax();

    @Query(value = "select min(y) from locations", nativeQuery = true)
    int yMin();
}
