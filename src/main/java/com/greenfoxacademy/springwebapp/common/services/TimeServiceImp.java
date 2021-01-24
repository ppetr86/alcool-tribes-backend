package com.greenfoxacademy.springwebapp.common.services;

import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImp implements TimeService {

  @Override
  public long getTime() {
    Instant instant = Instant.now(); //creates Instant object with actual time
    return instant.getEpochSecond();
  }

  @Override
  public long getTimeAfter(int sec) {
    return getTime() + sec;
  }

  @Override
  public int getTimeBetween(long from, long to) {
    return (int) (to - from);
  }
}
