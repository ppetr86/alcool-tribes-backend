package com.greenfoxacademy.springwebapp.common.services;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long getTime() {
    Instant instant = Instant.now();
    return instant.getEpochSecond();
  }

  @Override
  public long getTimeAfter(int sec) {
    return getTime() + sec;
  }

  @Override
  public int getTimeBetween(long from, long to) {
    if (to > from) {
      return (int) (to - from);
    } else {
      return 0;
    }
  }
}
