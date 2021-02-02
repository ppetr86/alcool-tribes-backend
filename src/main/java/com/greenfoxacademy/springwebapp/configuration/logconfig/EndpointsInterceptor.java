package com.greenfoxacademy.springwebapp.configuration.logconfig;

import static org.springframework.util.StringUtils.hasText;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Component
public class EndpointsInterceptor extends HandlerInterceptorAdapter {

/*  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    log.error(buildLogMessage(request, response));

    return true;
  }*/

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {

    log.error(buildLogMessage(request, response));

  }


  private String buildLogMessage(HttpServletRequest request, HttpServletResponse response) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String params = request.getQueryString();
    if(!hasText(params)) {params = "NULL";}
    String responseStatusCode = String.valueOf(response.getStatus());
    return String.format("HTTP Method: %s | URI: %s | Params: %s | Response Status Code: %s",
        method, uri, params, responseStatusCode);
  }

  public String buildSecurityErrorLogMessage(HttpServletRequest request, HttpServletResponse response, int statusCode, String text) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String params = request.getQueryString();
    if(!hasText(params)) {params = "NULL";}
    String responseStatusCode = String.valueOf(response.getStatus());
    return String.format("HTTP Method: %s | URI: %s | Params: %s | Response Status Code: %s | Message: %s",
        method, uri, params, statusCode, text);
  }

}
