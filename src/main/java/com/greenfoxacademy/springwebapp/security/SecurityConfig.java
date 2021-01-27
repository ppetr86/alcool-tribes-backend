package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.security.jwt.JwtFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private JwtFilter jwtFilter;
  private AuthenticationExceptionHandler authenticationExceptionHandler;
  public SecurityConfig (JwtFilter jwtFilter, AuthenticationExceptionHandler authenticationExceptionHandler) {
    this.jwtFilter = jwtFilter;
    this.authenticationExceptionHandler = authenticationExceptionHandler;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .httpBasic().disable()
        .csrf().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/register", "/login").permitAll() //permits these endpoints without auth.
        .anyRequest().authenticated() //any other endpoints requires authentication
        .and()
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(authenticationExceptionHandler); //here i put custom json together with 401
  }
}
