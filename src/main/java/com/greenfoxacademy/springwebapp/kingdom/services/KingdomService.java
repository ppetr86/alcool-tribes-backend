package com.greenfoxacademy.springwebapp.kingdom.services;

<<<<<<< HEAD
<<<<<<< HEAD
import com.greenfoxacademy.springwebapp.resource.models.ResourceEntity;
import org.springframework.stereotype.Service;
=======
>>>>>>> cb8251b9dbd875363981660cd7af425fafa75a78
=======
>>>>>>> cb8251b9dbd875363981660cd7af425fafa75a78
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;

import java.util.List;

@Service
public interface KingdomService {

  KingdomEntity findByPlayerId(Long id);

  KingdomEntity saveKingdom(KingdomEntity kingdom);

  List<ResourceEntity> getResources();

}
