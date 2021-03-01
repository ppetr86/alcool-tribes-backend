package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmail;

import javax.mail.MessagingException;

public interface EmailService {

  void sendMailWithHtmlAndPlainText(final AbstractEmail email) throws MessagingException;

}