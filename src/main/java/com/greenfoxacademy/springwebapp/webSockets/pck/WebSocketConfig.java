package com.greenfoxacademy.springwebapp.webSockets.pck;

import com.greenfoxacademy.springwebapp.kingdom.services.KingdomService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import com.greenfoxacademy.springwebapp.security.jwt.JwtProvider;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final static Logger logger = LogManager.getLogger(WebSocketConfig.class.getName());

  @Autowired
  private JwtProvider jwtProvider;
  @Autowired
  private KingdomService kingdomService;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  private static final String MESSAGE_PREFIX = "/topic";
  private static final String END_POINT = "/chat";
  private static final String APPLICATION_DESTINATION_PREFIX = "/live";


  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    if (registry != null) {
      registry.addEndpoint(END_POINT).setAllowedOrigins("*").withSockJS();
    }
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    if (registry != null) {
      registry.enableSimpleBroker(MESSAGE_PREFIX);
      registry.setApplicationDestinationPrefixes(APPLICATION_DESTINATION_PREFIX);
    }
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptor() {

      @SneakyThrows
      @Override
      public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
          String authToken = accessor.getFirstNativeHeader("Authorization");
          String jwt = "";
          if (authToken.startsWith("Bearer ")) {
            jwt = authToken.substring(7);
          }
          if (jwtProvider.validateToken(jwt)) {
            String userLogin = jwtProvider.getLoginFromToken(jwt);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            accessor.setDestination("/topic");
            logger.info(accessor.getDestination());
          }
        }
        return message;
      }
    });
  }
}
