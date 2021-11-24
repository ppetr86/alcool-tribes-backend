package com.greenfoxacademy.springwebapp.building.repositories;

import com.greenfoxacademy.springwebapp.building.models.BuildingEntity;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO;
import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV2DTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long>,
        JpaSpecificationExecutor<BuildingEntity> {

    List<BuildingEntity> findAll(Specification<BuildingEntity> specification);


    Page<BuildingEntity> findAll(Specification<BuildingEntity> specification, Pageable pageable);


    List<BuildingEntity> findAll(Specification<BuildingEntity> specification, Sort sort);


    List<BuildingEntity> findAllByKingdomId(Long kingdomID);


    @Query(value = "SELECT id FROM buildings WHERE id > :id ORDER BY id LIMIT 50", nativeQuery = true)
    List<Long> findAllIds(long id);


    @Query(value = "SELECT id FROM buildings", nativeQuery = true)
    List<Long> findAllIdsPaged(Pageable p);


    @Query("select new com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsV2DTO(b.id, b.level, b.hp, b.startedAt, b.finishedAt) " +
            "from BuildingEntity b where b.level >= :level AND b.hp >= :hp AND b.startedAt>= :startedAt")
    List<BuildingDetailsV2DTO> getBuildingDetailsDtoV2(@Param("level") Integer level, @Param("hp") Integer hp, @Param("startedAt") Long startedAt);


    //casting building type to string
    @Query("select new com.greenfoxacademy.springwebapp.building.models.dtos.BuildingDetailsDTO(b.id, CAST(b.type AS string), b.level, b.hp, b.startedAt, b.finishedAt) " +
            "from BuildingEntity b where b.level >= :level AND b.hp >= :hp AND b.startedAt>= :startedAt")
    List<BuildingDetailsDTO> getBuildingDetailsDtoV1(@Param("level") Integer level, @Param("hp") Integer hp, @Param("startedAt") Long startedAt);
}
