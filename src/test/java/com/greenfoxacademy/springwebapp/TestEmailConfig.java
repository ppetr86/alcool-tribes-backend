package com.greenfoxacademy.springwebapp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@TestConfiguration
public class TestEmailConfig implements WebMvcConfigurer {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private int port;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Bean
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(getHost());
    mailSender.setPort(getPort());

    mailSender.setUsername(getUsername());
    mailSender.setPassword(getPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
  }

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  public String getHost() {
    return host;
  }

  public String getUsername() {
    return username;
  }

  public int getPort() {
    return port;
  }

  public String getPassword() {
    return password;
  }
}
