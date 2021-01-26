package com.greenfoxacademy.springwebapp.common.services;

import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long getTime() {
    // TODO: epochTimeNow
    return 888L;
  }
}
