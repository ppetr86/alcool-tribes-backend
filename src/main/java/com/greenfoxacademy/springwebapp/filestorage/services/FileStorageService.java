package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String storeAvatar(MultipartFile file, PlayerEntity player) throws FileStorageException;

  Resource loadFileAsResource(String fileName, Authentication auth) throws MyFileNotFoundException,
      ForbiddenActionException;

  boolean userIsAllowedToAccessTheFile(String fileName, Authentication auth) throws ForbiddenActionException;

  String getLastPathFolderName();
}
