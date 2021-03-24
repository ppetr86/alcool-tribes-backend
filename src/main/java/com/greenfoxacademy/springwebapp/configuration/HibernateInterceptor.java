package com.greenfoxacademy.springwebapp.configuration;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.kingdom.models.dtos.KingdomResponseDTO;
import com.greenfoxacademy.springwebapp.websockets.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

@Slf4j
public class HibernateInterceptor extends EmptyInterceptor {

  @Autowired
  WebSocketHandler webSocketHandler;
  @Autowired
  ConvertService convertService;

  @Override
  public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
                              Object[] previousState, String[] propertyNames, Type[] types) {

    handleEntityForWebsocket(entity);
    return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
  }

  private void handleEntityForWebsocket(Object entity) {
    KingdomEntity kingdom = entityKingdom(entity);
    log.info("hibernate interceptor");
    if (kingdom != null && kingdom.getId() != null && webSocketHandler != null
        && webSocketHandler.hasSession(kingdom.getId())) {
      log.info("hibernate interceptor - should send websocket in a second");

      KingdomResponseDTO dto = convertService.convertKingdomToDTO(kingdom);
      String json = convertService.objectToJson(dto);
      webSocketHandler.sendMessage(kingdom.getId(), json);
    }
  }

  @Override
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

    handleEntityForWebsocket(entity);
    return super.onSave(entity, id, state, propertyNames, types);
  }

  private KingdomEntity entityKingdom(Object entity) {
    if (entity instanceof KingdomEntity) {
      return (KingdomEntity) entity;
    }

    try {
      return (KingdomEntity) entity.getClass().getMethod("getKingdom").invoke(entity);
    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }
}
