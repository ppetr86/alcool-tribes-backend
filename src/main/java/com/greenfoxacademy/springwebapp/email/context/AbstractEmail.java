package com.greenfoxacademy.springwebapp.email.context;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractEmail {

  private String recipientEmail;
  private String subject;
  private String senderEmail;
  private String senderDisplayName;
  private String username;
  private String templateLocationHtml;
  private String templateLocationText;

  private Map<String, Object> context;
  private String kingdomName;

  public AbstractEmail() {
    this.context = new HashMap<>();
  }

  public <T> void init(T context) {
  }

  public Object put(String key, Object value) {

    return key == null ? null : this.context.put(key.intern(), value);
  }
}