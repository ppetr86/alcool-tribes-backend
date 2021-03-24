package com.greenfoxacademy.springwebapp.configuration;

import com.greenfoxacademy.springwebapp.email.models.RegistrationTokenEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.location.models.LocationEntity;
import com.greenfoxacademy.springwebapp.webSockets.WebSocketHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.Serializable;

@Slf4j
public class HibernateInterceptor extends EmptyInterceptor {

  @SneakyThrows
  @Override
  public boolean onSave(Object entity, Serializable id,
                        Object[] state, String[] propertyNames, Type[] types) {

    log.info("session map size " + WebSocketHandler.sessionMap.size());
    if (entity instanceof RegistrationTokenEntity || entity instanceof LocationEntity) {
      log.info("hibernate interceptor for token and location");
      return false;

    } else if (entity instanceof KingdomEntity) {
      log.info("hibernate interceptor for kingdom entity");
      // this should send the websocket kingdom

      return ((KingdomEntity) entity).getId() != null && WebSocketHandler.sessionMap.containsKey(((KingdomEntity) entity).getId());

    } else if (entity.getClass().getMethod("getKingdom").invoke(entity) != null) {
      log.info("hibernate interceptor for entity containing kingdom");
      KingdomEntity kingdom = (KingdomEntity) entity.getClass().getMethod("getKingdom").invoke(entity);
      // this should send the websocket kingdom

      return WebSocketHandler.sessionMap.containsKey(kingdom.getId());

    }
    log.info("hibernate interceptor not covered by if else");
    return false;
  }
}
