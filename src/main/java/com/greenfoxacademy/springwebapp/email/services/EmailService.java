package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmail;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

  Boolean sendMailWithHtmlAndPlainText(final AbstractEmail email) throws MessagingException, IOException;

}