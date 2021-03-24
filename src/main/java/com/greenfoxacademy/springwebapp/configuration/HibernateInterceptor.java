package com.greenfoxacademy.springwebapp.configuration;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
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
  public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {

    KingdomEntity kingdom = entityKingdom(entity);
    if (kingdom != null && kingdom.getId() != null && webSocketHandler != null
        && webSocketHandler.hasSession(kingdom.getId())) {

      webSocketHandler.sendMessage(kingdom.getId(), convertService.objectToJson(kingdom));
    }
    return super.onSave(entity, id, state, propertyNames, types);
  }

  private KingdomEntity entityKingdom(Object entity) {
    if (entity instanceof KingdomEntity) {
      return (KingdomEntity) entity;
    }

    try {
      return (KingdomEntity) entity.getClass().getMethod("getKingdom").invoke(entity);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
    return null;
  }
}
