package com.greenfoxacademy.springwebapp.webSockets;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  private final HashMap<Long, WebSocketSession> sessionMap = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    String[] path = session.getUri().getPath().split("/");
    long id = Integer.parseInt(path[path.length-1]);

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
}