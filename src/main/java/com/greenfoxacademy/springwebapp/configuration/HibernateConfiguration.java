package com.greenfoxacademy.springwebapp.configuration;

import com.greenfoxacademy.springwebapp.websockets.WebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class HibernateConfiguration implements HibernatePropertiesCustomizer {

  WebSocketHandler webSocketHandler;
  ConvertService convertService;

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put("hibernate.session_factory.interceptor", kingdomInterceptor());
  }

  @Bean
  public HibernateInterceptor kingdomInterceptor() {
    return new HibernateInterceptor(webSocketHandler, convertService);
  }
}