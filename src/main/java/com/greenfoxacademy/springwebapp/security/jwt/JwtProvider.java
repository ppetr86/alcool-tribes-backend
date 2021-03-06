package com.greenfoxacademy.springwebapp.security.jwt;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;
  @Value("${jwt.expire-days}")
  private int jwtExpireDays;

  public String generateToken(PlayerEntity playerEntity) {
    //creating Expiration date - by using LocalDate, which is preferred
    Date date = Date.from(LocalDate.now().plusDays(jwtExpireDays).atStartOfDay(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
        .setClaims(new HashMap<String, Object>() {
          {
            put("username", playerEntity.getUsername());
            put("kingdomId", playerEntity.getKingdom().getId());
          }
        })
        .setExpiration(date)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public boolean validateToken(String token) throws Exception {
    Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
    return true;
  }

  public String getLoginFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.get("username", String.class);
  }
}