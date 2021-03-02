package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmail;

import javax.mail.MessagingException;

public interface EmailService {

  Boolean sendMailWithHtmlAndPlainText(final AbstractEmail email) throws MessagingException;

}