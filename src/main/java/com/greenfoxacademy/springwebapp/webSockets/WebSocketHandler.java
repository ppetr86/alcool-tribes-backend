package com.greenfoxacademy.springwebapp.webSockets;

import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

  private final HashMap<Long, WebSocketSession> sessionMap = new HashMap<>();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    TimeUnit.SECONDS.sleep(1);
    KingdomEntity kingdom = ((CustomUserDetails) session.getPrincipal()).getKingdom();
    sessionMap.put(kingdom.getId(), session);
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