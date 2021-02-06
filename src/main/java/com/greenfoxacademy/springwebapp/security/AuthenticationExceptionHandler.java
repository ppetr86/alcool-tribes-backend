package com.greenfoxacademy.springwebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint, Serializable {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException, ServletException {
    ObjectMapper mapper = new ObjectMapper();
    String responseMsg = mapper.writeValueAsString(new ErrorDTO("Unauthorised request."));

    response.getWriter().write(responseMsg);
    response.addHeader("Content-Type","application/json");
    response.setStatus(SecurityConfig.AUTHENTICATION_FAILURE_STATUSCODE);
  }
}
