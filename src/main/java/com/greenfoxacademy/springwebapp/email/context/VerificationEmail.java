package com.greenfoxacademy.springwebapp.email.context;

import com.greenfoxacademy.springwebapp.player.controllers.PlayerController;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.util.UriComponentsBuilder;

@Getter
@Setter
public class VerificationEmail extends AbstractEmail {

  private String token;
  @Value("${senderEmail}")
  private String senderEmail;

  @Value("${senderDisplayName}")
  private String senderDisplayName;
  @Value("${subject}")
  private String subject;

  @Override
  public <T> void init(T context) {
    PlayerEntity player = (PlayerEntity) context;
    // we pass the variables we want in Thymeleaf in the context map
    put("username", player.getUsername());
    put("kingdomname", player.getKingdom().getKingdomName());

    setSenderEmail(senderEmail);
    setSenderDisplayName(senderDisplayName);

    setRecipientEmail(player.getEmail());
    setKingdomName(player.getKingdom().getKingdomName());
    setUsername(player.getUsername());
    setTemplateLocation("registration");
    setSubject(subject);
  }

  public void setToken(String token) {
    this.token = token;
    put("token", token);
  }

  public void buildVerificationUrl(final String baseURL, final String token) {
    final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
            .path(PlayerController.URIVERIFY).queryParam("token", token).toUriString();
    put("verificationURL", url);
  }
}