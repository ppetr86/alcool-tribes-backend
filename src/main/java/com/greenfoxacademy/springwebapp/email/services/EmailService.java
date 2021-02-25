package com.greenfoxacademy.springwebapp.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

  void sendMailWithHtmlAndPlainText(final AbstractEmailContext email) throws MessagingException;

  void sendTextEmail(AbstractEmailContext email)throws MessagingException ;

  }
