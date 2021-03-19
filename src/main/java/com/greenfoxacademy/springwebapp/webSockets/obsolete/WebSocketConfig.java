/*
package com.greenfoxacademy.springwebapp.webSockets.obsolete;

import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.thymeleaf.util.StringUtils;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService customUserDetailsService;

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {

      @SneakyThrows
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          String jwtToken = accessor.getFirstNativeHeader("Authorization");
          if (!StringUtils.isEmpty(jwtToken)) {
            String authToken = jwtProvider.getLoginFromToken(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            accessor.setUser(authToken);
          }
        }

        return message;
      }
    });
  }
}*/
