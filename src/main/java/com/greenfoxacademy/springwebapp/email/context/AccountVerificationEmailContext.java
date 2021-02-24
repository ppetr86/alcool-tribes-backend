package com.greenfoxacademy.springwebapp.email.context;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext {

  private String token;

  @Override
  public <T> void init(T context) {
    //we can do any common configuration setup here
    // like setting up some base URL and context
    PlayerEntity player = (PlayerEntity) context; // we pass the player information
    put("firstName", player.getEmail());
    put("username", player.getUsername());
    put("kingdomname", player.getKingdom().getKingdomName());
    put("to", player.getEmail());
    put("senderEmail","2abbedeb1d-3b2376@inbox.mailtrap.io");
    put("senderDisplayName", "AlcoolGame");
    put("emailLanguage", "English");

    //setTemplateLocation("templates/registration");
    setSubject("Verify your email for Alcool Game");
    setFrom("AlcoolGame");

    setRecipientEmail(player.getEmail());
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