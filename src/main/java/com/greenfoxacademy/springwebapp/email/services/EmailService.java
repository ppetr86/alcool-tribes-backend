package com.greenfoxacademy.springwebapp.configuration.email.services;

import com.greenfoxacademy.springwebapp.email.context.AbstractEmailContext;

import javax.mail.MessagingException;

public interface EmailService {

  void sendMail(final AbstractEmailContext email) throws MessagingException;

  void sendTextEmail(AbstractEmailContext email)throws MessagingException ;

  }
