package com.greenfoxacademy.springwebapp.resource.repositories;

import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {

    List<ResourceResponseDTO> findAllByKingdomId(long id);
}
