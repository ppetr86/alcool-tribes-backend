/*
package com.greenfoxacademy.springwebapp.webSockets.obsolete;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthChannelInterceptorAdapter implements ChannelInterceptor {
  private static  String USERNAME_HEADER = "login";
  private static String PASSWORD_HEADER = "passcode";
  private final com.greenfoxacademy.springwebapp.springsockets.obsolete.WebSocketAuthenticatorService
      webSocketAuthenticatorService;

  @Override
  public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT == accessor.getCommand()) {
      final String username = accessor.getFirstNativeHeader(USERNAME_HEADER);
      final String password = accessor.getFirstNativeHeader(PASSWORD_HEADER);

      final UsernamePasswordAuthenticationToken
          user = webSocketAuthenticatorService.getAuthenticatedOrFail(username, password);

      accessor.setUser(user);
    }
    return message;
  }
}
*/
