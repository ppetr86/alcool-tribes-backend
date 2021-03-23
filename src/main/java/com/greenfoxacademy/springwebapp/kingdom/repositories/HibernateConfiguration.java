package com.greenfoxacademy.springwebapp.kingdom.repositories;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class HibernateConfiguration implements HibernatePropertiesCustomizer {

  @Override
  public void customize(Map<String, Object> hibernateProperties) {
    hibernateProperties.put("hibernate.session_factory.interceptor", kingdomInterceptor());
  }


  @Bean
  public KingdomInterceptor kingdomInterceptor(){
    return new KingdomInterceptor();
  }

}
