package com.greenfoxacademy.springwebapp.globalexceptionhandling;

public class FileStorageException extends RuntimeException {

  public FileStorageException(String str) {
    super(str);
  }

  public FileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
