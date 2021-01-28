package com.greenfoxacademy.springwebapp.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Log
@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String jwtSecret;

  public String generateToken(String userName) {
    //creating Expiration date - by using LocalDate, which is preferred
    Date date = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    return Jwts.builder()
            .setSubject(userName)
            .setExpiration(date)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
      return true;
    } catch (ExpiredJwtException e) {
      log.severe("invalid token: token expired");
    } catch (UnsupportedJwtException e) {
      log.severe("invalid token: format of received token is not supported");
    } catch (MalformedJwtException e) {
      log.severe("invalid token: JWT couldn't reconstruct token content");
    } catch (SignatureException e) {
      log.severe("invalid token: JWT failed to calculate digital signature of token");
    } catch (IllegalArgumentException e) {
      log.severe("invalid token: token has passed invalid argument to the method");
    }
    return false;
  }

  public String getLoginFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

}
