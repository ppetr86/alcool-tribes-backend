package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String storeAvatar(MultipartFile file, PlayerEntity player);
}
