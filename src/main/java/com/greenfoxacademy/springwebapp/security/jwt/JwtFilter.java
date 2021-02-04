package com.greenfoxacademy.springwebapp.security.jwt;

import static org.springframework.util.StringUtils.hasText;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Log
@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

  public static final String AUTHORIZATION = "Authorization";

  private JwtProvider jwtProvider;
  private CustomUserDetailsService customUserDetailsService;
  private PlayerService playerService;

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    String token = getTokenFromServletRequest((HttpServletRequest) servletRequest);
    if (token != null && jwtProvider.validateToken(token)) {
      String userLogin = jwtProvider.getLoginFromToken(token);
      PlayerEntity player = playerService.findByUsername(userLogin);
      CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
      customUserDetails.setKingdom(player.getKingdom());
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
          null, customUserDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(auth);
      logger.info("Following player was authenticated: " + customUserDetails.getUsername());
    }
    filterChain.doFilter(servletRequest, servletResponse);

  }

  private String getTokenFromServletRequest(HttpServletRequest servletRequest){
    String bearerToken = servletRequest.getHeader(AUTHORIZATION);
    if (hasText(bearerToken) && bearerToken.startsWith("Bearer ")){ //hasText means "is not null or empty"
      return bearerToken.substring(7);
    }
    return null;
  }
}
