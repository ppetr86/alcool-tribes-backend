package com.greenfoxacademy.springwebapp.webSockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

  public final static String CHAT_SPECIFIC_USER = "/kingdom-updated";

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
    webSocketHandlerRegistry.addHandler(getHandler(), CHAT_SPECIFIC_USER)
        .setAllowedOrigins("*");
  }

  @Bean
  public WebSocketHandler getHandler(){
    return new WebSocketHandler();
  }
}
