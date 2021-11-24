package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String getAvatarsFolderName();


    String getFileUrl(String folderName, String fileName);


    Resource loadFileAsResource(String fileName, Authentication auth) throws MyFileNotFoundException,
            ForbiddenActionException;


    String storeAvatar(MultipartFile file, PlayerEntity player) throws FileStorageException,
            WrongContentTypeException;


    boolean userIsAllowedToAccessTheFile(String fileName, Authentication auth) throws ForbiddenActionException;
}
