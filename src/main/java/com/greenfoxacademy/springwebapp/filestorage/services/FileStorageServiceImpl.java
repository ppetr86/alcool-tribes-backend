package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.configuration.filestorageconfig.FileStorageProperties;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  private final Path fileStorageLocation;
  FileStorageProperties fileStorageProperties;

  //injecting dependency to fileStorageProperties + using it immediately for defining fileStorageLocation
  public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
    this.fileStorageProperties = fileStorageProperties;

    this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadAvatarDir())
        .toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.fileStorageLocation);
    } catch (Exception ex) {
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  @Override
  public String storeAvatar(MultipartFile file, PlayerEntity player) throws FileStorageException {
    // Normalize file name
    String fileName = StringUtils.cleanPath("AVATAR_" + player.getId() + "_"
        + player.getUsername() + "_" + file.getOriginalFilename());

    try {
      // Check if the file's name contains invalid characters
      if(fileName.contains("..")) {
        throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
      }

      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = this.fileStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      return fileName;
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
    }
  }

  @Override
  public Resource loadFileAsResource(String fileName, Authentication auth)
      throws MyFileNotFoundException, ForbiddenActionException {
    try {
      Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if(resource.exists()) {
        userIsAllowedToAccessTheFile(fileName, auth);
        return resource;
      } else {
        throw new MyFileNotFoundException("File not found " + fileName);
      }
    } catch (MalformedURLException ex) {
      throw new MyFileNotFoundException("File not found " + fileName);
    }
  }

  @Override
  public boolean userIsAllowedToAccessTheFile(String fileName, Authentication auth)
      throws ForbiddenActionException {

    String userId = ((CustomUserDetails) auth.getPrincipal()).getPlayer().getId().toString();
    int firstIndex = fileName.indexOf("_");
    int secondIndex = fileName.indexOf("_",firstIndex + 1);

    if (fileName.substring(firstIndex + 1, secondIndex).equals(userId)) return true;

    throw new ForbiddenActionException();
  }

  @Override
  public String getLastPathFolderName() {
    String target = fileStorageLocation.toString();

    List<Character> folderPath = target.chars()
        .mapToObj(a -> (char)a)
        .collect(Collectors.toList());
    int lastFolderIndex = 0;

    for (int i = folderPath.size()-1; i>0; i--) {
      if (folderPath.get(i).equals('\\')) {
        lastFolderIndex = i;
        break;
      }
    }

    return target.substring(lastFolderIndex+1);
  }
}
