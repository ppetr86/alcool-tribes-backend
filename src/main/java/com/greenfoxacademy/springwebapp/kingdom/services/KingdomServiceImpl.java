package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.building.models.dtos.BuildingSingleResponseDTO;
import com.greenfoxacademy.springwebapp.configuration.ConvertService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.IdNotFoundException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomNameDTO;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.location.models.dtos.LocationEntityDTO;
import com.greenfoxacademy.springwebapp.resource.models.dtos.ResourceResponseDTO;
import com.greenfoxacademy.springwebapp.troop.models.dtos.TroopEntityResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class KingdomServiceImpl implements KingdomService {

    private final KingdomRepository kingdomRepository;
    private final ConvertService convertService;

    @Override
    public KingdomResponseDTO changeKingdomName(KingdomEntity kingdom, KingdomNameDTO nameDTO) {
        kingdom.setKingdomName(nameDTO.getName());
        saveKingdom(kingdom);
        return convert(kingdom);
    }

    @Override
    public KingdomResponseDTO convert(KingdomEntity e) {
        return KingdomResponseDTO.builder()
                .withId(e.getId())
                .withName(e.getKingdomName())
                .withUserId(e.getPlayer().getId())
                .withBuildings(e.getBuildings().stream()
                        .map(BuildingSingleResponseDTO::new)
                        .collect(toList()))
                .withResources(e.getResources().stream()
                        .map(ResourceResponseDTO::new)
                        .collect(toList()))
                .withTroops(e.getTroops().stream()
                        .map(TroopEntityResponseDTO::new)
                        .collect(toList()))
                .withLocation(new LocationEntityDTO(e.getLocation()))
                .build();
    }

    @Override
    public KingdomResponseDTO entityToKingdomResponseDTO(Long id) throws IdNotFoundException {
        KingdomEntity kingdom = findByID(id);
        if (kingdom == null) {
            throw new IdNotFoundException();
        }
        return convert(kingdom);
    }

    @Override
    public KingdomEntity findByID(Long id) {
        return kingdomRepository.findById(id).orElse(null);
    }

    @Override
    public KingdomEntity findByPlayerId(Long id) {
        return null;
    }

    @Override
    public String findKingdomNameByPlayerID(Long id) {
        return kingdomRepository.findKingdomNameByPlayerID(id);
    }

    @Override
    public List<KingdomResponseDTO> kingomWithNameLike(String name) {
        Specification<KingdomEntity> nameLike = (root, query, cb) -> cb.like(root.get("kingdomName"), "%" + name + "%");
        return kingdomRepository.findAll(nameLike).stream().map(convertService::convertKingdomToDTO).collect(toList());
    }

    @Override
    public KingdomEntity saveKingdom(KingdomEntity kingdom) {
        kingdom = kingdomRepository.save(kingdom);
        return kingdom;
    }

}