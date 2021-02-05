package com.greenfoxacademy.springwebapp.security.jwt;

import static org.springframework.util.StringUtils.hasText;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;

import com.greenfoxacademy.springwebapp.configuration.logconfig.EndpointsInterceptor;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import com.greenfoxacademy.springwebapp.security.SecurityConfig;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION = "Authorization";

  private JwtProvider jwtProvider;
  private CustomUserDetailsService customUserDetailsService;
  private EndpointsInterceptor endpointsInterceptor;
  private PlayerService playerService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String token = getTokenFromServletRequest(request);
    Boolean tokenIsValid = false;

    try{
      tokenIsValid = jwtProvider.validateToken(token);
    } catch (Exception e) {
      SecurityContextHolder.clearContext(); //we are clearing context before throwing Exception
      log.error(endpointsInterceptor.buildSecurityErrorLogMessage(
          request,
          response,
          SecurityConfig.AUTHENTICATIONFAILURESTATUSCODE,
          "Token validation error"
      ));
    }

    if (token != null && tokenIsValid) {
      String userLogin = jwtProvider.getLoginFromToken(token);
      PlayerEntity player = playerService.findByUsername(userLogin);
      CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
      customUserDetails.setKingdom(player.getKingdom());
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
          null, customUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
      log.info("Authenticated player: {}", customUserDetails.getUsername());
    }

    filterChain.doFilter(request, response);
  }

  private String getTokenFromServletRequest(HttpServletRequest servletRequest){
    String bearerToken = servletRequest.getHeader(AUTHORIZATION);
    if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")){ //hasText means "is not null or empty"
      return bearerToken.substring(7);
    }
    return null;
  }
}
