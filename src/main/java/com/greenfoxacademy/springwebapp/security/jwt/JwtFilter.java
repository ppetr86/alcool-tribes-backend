package com.greenfoxacademy.springwebapp.security.jwt;

import com.greenfoxacademy.springwebapp.configuration.logconfig.EndpointsInterceptor;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import com.greenfoxacademy.springwebapp.security.CustomUserDetailsService;
import com.greenfoxacademy.springwebapp.security.SecurityConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  public static final String AUTHORIZATION = "Authorization";

  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService customUserDetailsService;
  private final EndpointsInterceptor endpointsInterceptor;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer")) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = getTokenFromServletRequest(request);
    Boolean tokenIsValid = validateToken(token, request, response);
    if (token != null && tokenIsValid) {
      authenticateUser(token);
    }
    filterChain.doFilter(request, response);
  }

  private void authenticateUser(String token) {
    String userLogin = jwtProvider.getLoginFromToken(token);
    CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(customUserDetails,
        null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    log.info("Authenticated player: {}", customUserDetails.getUsername());
  }

  private Boolean validateToken(String token, HttpServletRequest request, HttpServletResponse response) {
    Boolean tokenIsValid = false;
    try {
      tokenIsValid = jwtProvider.validateToken(token);
    } catch (Exception e) {
      cleanContextAndLogErrorMessage(request, response);
    }
    return tokenIsValid;
  }

  private void cleanContextAndLogErrorMessage(HttpServletRequest request, HttpServletResponse response) {
    SecurityContextHolder.clearContext(); //we are clearing context before throwing Exception
    //Specific message related to authentication failure. Otherwise when wrong token
    // no log is created by interceptor at all.
    log.error(endpointsInterceptor.buildSecurityErrorLogMessage(
        request,
        response,
        SecurityConfig.AUTHENTICATION_FAILURE_STATUSCODE,
        "Token validation error"
    ));
  }

  private String getTokenFromServletRequest(HttpServletRequest servletRequest) {
    String bearerToken = servletRequest.getHeader(AUTHORIZATION);
    if (bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}