package com.greenfoxacademy.springwebapp.kingdom.services;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.repositories.KingdomRepository;
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import com.greenfoxacademy.springwebapp.resource.services.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KingdomServiceImpl implements KingdomService {
  private KingdomRepository kingdomRepository;
  private ResourceService resourceService;

  public KingdomServiceImpl(KingdomRepository kingdomRepository,
                            ResourceService resourceService) {
    this.kingdomRepository = kingdomRepository;
    this.resourceService = resourceService;
  }


  @Override
  public KingdomEntity findByPlayerId(Long id) {
    return null;
  }

  @Override
  public KingdomEntity saveKingdom(KingdomEntity kingdom) {
    return kingdomRepository.save(kingdom);
  }
<<<<<<< HEAD
<<<<<<< HEAD

  @Override
  public List<ResourceEntity> getResources() {
    return resourceService.findAllResources();
  }
}
=======
}
>>>>>>> cb8251b9dbd875363981660cd7af425fafa75a78
=======
}
>>>>>>> cb8251b9dbd875363981660cd7af425fafa75a78
