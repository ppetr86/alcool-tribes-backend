package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

  @Override
  public String storeAvatar(MultipartFile file, PlayerEntity player) {

    return null;
  }
}
