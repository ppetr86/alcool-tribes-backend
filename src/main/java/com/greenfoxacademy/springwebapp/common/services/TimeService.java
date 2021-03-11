package com.greenfoxacademy.springwebapp.common.services;

public interface TimeService {
  long getTime();

  long getTimeAfter(int sec);

  int getTimeBetween(long from, long to);
}
