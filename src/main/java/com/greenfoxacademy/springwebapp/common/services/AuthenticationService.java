package com.greenfoxacademy.springwebapp.common.services;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {
  boolean hasAccess(Long id);
}
