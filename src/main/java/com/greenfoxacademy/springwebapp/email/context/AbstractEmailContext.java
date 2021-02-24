package com.greenfoxacademy.springwebapp.email.context;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractEmailContext {

    private String from;
    private String recipientEmail;
    private String subject;
    private String senderEmail;
    private String senderDisplayName;
    private String emailLanguage;
    private String username;
    private String templateLocation;
    private String kingdomName;
    private Map<String, Object> context;


    public AbstractEmailContext() {
        this.context = new HashMap<>();
    }

    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
    }

    public Object put(String key, Object value) {
        return key ==null ? null : this.context.put(key.intern(),value);
    }
}