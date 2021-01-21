package com.greenfoxacademy.springwebapp.security.jwt;

import io.jsonwebtoken.*;
import lombok.Value;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Log
@Component
public class JwtProvider {


  private String jwtSecret;

  public String generateToken(String userName){
    Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());//creating Expiration date - by using LocalDate, which is preferred
    return Jwts.builder()
      .setSubject(userName)
      .setExpiration(date)
      .signWith(SignatureAlgorithm.HS512,jwtSecret)
      .compact();
  }

}
