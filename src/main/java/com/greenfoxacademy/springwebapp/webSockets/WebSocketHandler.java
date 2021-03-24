package com.greenfoxacademy.springwebapp.webSockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

  public static final HashMap<Long, WebSocketSession> sessionMap = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String[] path = session.getUri().getPath().split("/");
    long id = Integer.parseInt(path[path.length - 1]);
    log.info("websocket sessionmap updated. Kingdom ID added: " + id);
    sessionMap.put(id, session);
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    for (Map.Entry each : sessionMap.entrySet()) {
      WebSocketSession wss = (WebSocketSession) each.getValue();
      wss.sendMessage(message);
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessionMap.remove(session);
  }

  public boolean hasSession(Long id) {
    return sessionMap.containsKey(id);
  }

  public boolean sendMessage(long id, String json) {
    WebSocketSession session = sessionMap.get(id);
    try {
      session.sendMessage(new TextMessage(json));
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      log.info("unable to send websocket message");
      return false;
    }
  }
}