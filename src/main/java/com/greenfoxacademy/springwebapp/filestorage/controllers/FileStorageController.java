package com.greenfoxacademy.springwebapp.filestorage.controllers;

import com.greenfoxacademy.springwebapp.filestorage.models.dtos.UploadFileResponseDTO;
import com.greenfoxacademy.springwebapp.filestorage.services.FileStorageService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@AllArgsConstructor
@RestController
public class FileStorageController {

  public static final String AVATAR_URI = "/images/avatar";
  public static final String AVATARS_URI = "/images/avatars";

  private FileStorageService fileStorageService;

  @PostMapping(AVATAR_URI)
  public UploadFileResponseDTO uploadAvatar(@RequestParam("file") MultipartFile file, Authentication auth)
      throws FileStorageException {
    PlayerEntity player = ((CustomUserDetails) auth.getPrincipal()).getPlayer();

    String fileName = fileStorageService.storeAvatar(file, player);

    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
        .path(fileStorageService.getLastPathFolderName()+"/")
        .path(fileName)
        .toUriString();

    return new UploadFileResponseDTO(fileName, fileDownloadUri,
        file.getContentType(), file.getSize());

  }

  @PostMapping(AVATARS_URI)
  public List<UploadFileResponseDTO> uploadMultipleAvatars(@RequestParam("file") MultipartFile[] files, Authentication auth) {
    return Arrays.asList(files)
        .stream()
        .map(file -> uploadAvatar(file, auth))
        .collect(Collectors.toList());
  }

  @GetMapping("/avatars/{fileName:.+}")
  public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request, Authentication auth)
      throws ForbiddenActionException, MyFileNotFoundException {

    // Load file as Resource
    Resource resource = fileStorageService.loadFileAsResource(fileName, auth);

    // Try to determine file's content type
    String contentType = null;
    try {
      contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
    } catch (IOException ex) {
      log.info("Could not determine file type.");
    }

    // Fallback to the default content type if type could not be determined
    if(contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
        .body(resource);
  }

}
