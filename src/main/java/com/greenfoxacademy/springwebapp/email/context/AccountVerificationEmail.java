package com.greenfoxacademy.springwebapp.email.context;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class AccountVerificationEmail extends AbstractEmail {

  private String token;

  @Override
  public <T> void init(T context) {
    PlayerEntity player = (PlayerEntity) context;
    // we pass the variables we want in Thymeleaf in the context map
    put("username", player.getUsername());
    put("kingdomname", player.getKingdom().getKingdomName());

    setSenderEmail("2abbedeb1d-3b2376@inbox.mailtrap.io");
    setSenderDisplayName("AlcoolGame");

    setRecipientEmail(player.getEmail());
    setRecipientEmail(player.getEmail());

    setKingdomName(player.getKingdom().getKingdomName());
    setUsername(player.getUsername());
    setTemplateLocation("registration");
    setSubject("Verify your email for Alcool Game");
  }

  public void setToken(String token) {
    this.token = token;
    put("token", token);
  }

  public void buildVerificationUrl(final String baseURL, final String token) {
    final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
            .path("/register/verify").queryParam("token", token).toUriString();
    put("verificationURL", url);
  }
}