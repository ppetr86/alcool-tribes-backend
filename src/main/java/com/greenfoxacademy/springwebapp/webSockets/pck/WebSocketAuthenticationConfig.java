package com.greenfoxacademy.springwebapp.webSockets.pck;

import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
@AllArgsConstructor
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

  private JwtProvider jwtProvider;
  private KingdomService kingdomService;
  private CustomUserDetailsService customUserDetailsService;

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {

    registration.interceptors(new ChannelInterceptor() {
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          List<String> authorization = accessor.getNativeHeader("Authorization");
          log.debug("Authorization: {}", authorization);

          String token = authorization.get(0).substring(7);

          String userLogin = jwtProvider.getLoginFromToken(token);
          CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
          UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
              null, customUserDetails.getAuthorities());
          SecurityContextHolder.getContext().setAuthentication(auth);
          accessor.setDestination("/topic");
          log.info(accessor.getDestination());
        }
        return message;
      }
    });
  }
}
