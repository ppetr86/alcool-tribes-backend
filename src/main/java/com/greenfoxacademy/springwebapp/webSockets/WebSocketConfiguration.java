package com.greenfoxacademy.springwebapp.webSockets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  public final static String KINGDOM_UPDATED = "/kingdom-update";
  @Autowired
  private WebSocketHandler webSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    webSocketHandlerRegistry.addHandler(webSocketHandler, KINGDOM_UPDATED )
        .withSockJS();
    webSocketHandlerRegistry.addHandler(webSocketHandler, KINGDOM_UPDATED);
  }

}
