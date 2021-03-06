package com.greenfoxacademy.springwebapp.configuration.logconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class EndpointsInterceptor extends HandlerInterceptorAdapter {

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {

    log.info(buildLogMessage(request, response));

  }

  private String buildLogMessage(HttpServletRequest request, HttpServletResponse response) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String params = request.getQueryString();
    if (!hasText(params)) {
      params = "NULL";
    }
    String responseStatusCode = String.valueOf(response.getStatus());
    return String.format("HTTP Method: %s | URI: %s | Params: %s | Response Status Code: %s",
        method, uri, params, responseStatusCode);
  }

  //Specific message related to authentication failure.
  // Otherwise when wrong token no log is created by interceptor at all.
  public String buildSecurityErrorLogMessage(HttpServletRequest request, HttpServletResponse response, int statusCode,
                                             String text) {
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String params = request.getQueryString();
    if (!hasText(params)) {
      params = "NULL";
    }
    String responseStatusCode = String.valueOf(response.getStatus());
    return String.format("HTTP Method: %s | URI: %s | Params: %s | Response Status Code: %s | Message: %s",
        method, uri, params, statusCode, text);
  }

}
