package com.greenfoxacademy.springwebapp.configuration.logconfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class EndpointsInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    super.afterCompletion(request, response, handler, ex);
  }
}
