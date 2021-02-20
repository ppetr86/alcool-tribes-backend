package com.greenfoxacademy.springwebapp.configuration.logconfig;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class EndpointMVCConfig implements WebMvcConfigurer {
  EndpointsInterceptor endpointsInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(endpointsInterceptor);
  }
}