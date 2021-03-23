package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.webSockets.WebSocketHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

@Slf4j
public class KingdomInterceptor extends EmptyInterceptor {

  @SneakyThrows
  @Override
  public boolean onSave(Object entity, Serializable id,
                        Object[] state, String[] propertyNames, Type[] types) {

    if (entity.getClass().getMethod("getKingdom").invoke(entity) != null) {
      KingdomEntity kingdom = (KingdomEntity) entity.getClass().getMethod("getKingdom").invoke(entity);
      if (kingdom.getId() != null) {
        if (WebSocketHandler.sessionMap.containsKey(kingdom.getId())) {
          log.info("Intercepted hibernate save/update of Entity which contains KingdomEntity");
          return false;
        }
      }
    } else if (entity instanceof KingdomEntity && ((KingdomEntity) entity).getId() != null) {
      KingdomEntity kingdom = (KingdomEntity) entity;
      if (kingdom.getId() != null) {
        if (WebSocketHandler.sessionMap.containsKey(kingdom.getId())) {
          log.info("Intercepted hibernate save/update of existing KingdomEntity");
          return true;
        }
      }
    } else if (entity instanceof KingdomEntity && ((KingdomEntity) entity).getId() == null) {
      log.info("Intercepted hibernate save of newly created KingdomEntity without ID");
      return false;
    }
    return false;
  }
}
