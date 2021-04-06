package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.configuration.filestorageconfig.FileStorageProperties;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {
  private final Path avatarStorageLocation;
  FileStorageProperties fileStorageProperties;
  PlayerService playerService;

  //injecting dependency to fileStorageProperties + using it immediately for defining fileStorageLocation
  //@Lazy - to prevent forming of a cycle of DI (in PlayerService there is also DI to FileStorageService)
  public FileStorageServiceImpl(FileStorageProperties fileStorageProperties, @Lazy PlayerService playerService) {
    this.fileStorageProperties = fileStorageProperties;
    this.playerService = playerService;

    this.avatarStorageLocation = Paths.get(fileStorageProperties.getUploadAvatarDir())
        .toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.avatarStorageLocation);
    } catch (Exception ex) {
      throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
    }
  }

  @Override
  public String storeAvatar(MultipartFile file, PlayerEntity player)
      throws FileStorageException, WrongContentTypeException {
    // Normalize file name
    String fileName = StringUtils.cleanPath("AVATAR_" + player.getId() + "_"
        + player.getUsername() + "_" + file.getOriginalFilename());
    try {
      // Check if the file's name contains invalid characters and is of type image
      checkCorrectnessOfFileNameAndFileType(fileName,file);
      // Copy file to the target location (Replacing existing file with the same name)
      Path targetLocation = this.avatarStorageLocation.resolve(fileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      // Save avatar address into database
      player.setAvatar(fileName);
      playerService.savePlayer(player);

      return fileName;
    } catch (IOException ex) {
      throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
    }
  }

  public void checkCorrectnessOfFileNameAndFileType(String fileName, MultipartFile file)
      throws FileStorageException, WrongContentTypeException {
    if (fileName.contains("..")) {
      throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
    }
    if (!file.getContentType().contains("image")) {
      throw new WrongContentTypeException("Other than image files are not allowed! : " + file.getContentType());
    }
  }

  @Override
  public Resource loadFileAsResource(String fileName, Authentication auth)
      throws MyFileNotFoundException, ForbiddenActionException {
    try {
      Path filePath = this.avatarStorageLocation.resolve(fileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        if (fileName.equals("AVATAR_0_generic.png")) {
          return resource;
        } else {
          userIsAllowedToAccessTheFile(fileName, auth);
          return resource;
        }
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
  public String getAvatarsFolderName() {
    String target = avatarStorageLocation.toString();

    List<Character> folderPath = target.chars()
        .mapToObj(a -> (char)a)
        .collect(Collectors.toList());
    int lastFolderIndex = 0;

    for (int i = folderPath.size() - 1; i > 0; i--) {
      if (folderPath.get(i).equals('\\')) {
        lastFolderIndex = i;
        break;
      }
    }

    return target.substring(lastFolderIndex + 1);
  }
}
