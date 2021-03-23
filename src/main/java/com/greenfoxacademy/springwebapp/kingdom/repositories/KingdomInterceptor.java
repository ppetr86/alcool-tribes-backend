package com.greenfoxacademy.springwebapp.kingdom.repositories;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.webSockets.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

@Slf4j
public class KingdomInterceptor extends EmptyInterceptor {

  @Override
  public boolean onSave(Object entity, Serializable id,
                        Object[] state, String[] propertyNames, Type[] types) {

    if (entity instanceof KingdomEntity) {
      log.info("session map size " + WebSocketHandler.sessionMap.size());
      KingdomEntity kingdom = ((KingdomEntity) entity);
      if (kingdom.getId() != null) {
        long kingdomID = ((KingdomEntity) entity).getId();
        if (WebSocketHandler.sessionMap.containsKey(kingdomID)) {
          log.info("Intercepted hibernate save/update of KingdomEntity");
          return true;
        }
      }
      log.info("Intercepted hibernate save of newly created KingdomEntity without ID");
    }
    return false;
  }
}
