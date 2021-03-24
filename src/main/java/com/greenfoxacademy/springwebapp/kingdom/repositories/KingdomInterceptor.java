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

    if (entity instanceof KingdomEntity || entity.getClass().getMethod("getKingdom").invoke(entity) != null) {
      log.info("session map size " + WebSocketHandler.sessionMap.size());
      if (entity instanceof KingdomEntity) {
        return ((KingdomEntity) entity).getId() != null && WebSocketHandler.sessionMap.containsKey(((KingdomEntity) entity).getId());
      } else {
        KingdomEntity kingdom = (KingdomEntity) entity.getClass().getMethod("getKingdom").invoke(entity);
        return WebSocketHandler.sessionMap.containsKey(kingdom.getId());
      }
    }

    return false;
  }
}
