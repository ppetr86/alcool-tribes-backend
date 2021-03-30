package com.greenfoxacademy.springwebapp.filestorage.controllers;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;


import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.filestorage.models.dtos.UploadFileResponseDTO;
import com.greenfoxacademy.springwebapp.filestorage.services.FileStorageService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;

// Note: integration test annotations needed since tests require web context
@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileStorageControllerTest {

  private FileStorageController fileStorageController;
  private FileStorageService fileStorageService;
  private Authentication authentication;

  @Before
  public void setup() {
    authentication = createAuth("test", 1L);
    fileStorageService = Mockito.mock(FileStorageService.class);
    fileStorageController = new FileStorageController(fileStorageService);
  }

  @Test
  public void uploadAvatar_imageContent_returnsCorrectDTO() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "image/png", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    Mockito.when(fileStorageService.storeAvatar(file, player)).thenReturn("AVATAR_1_avatar.png");
    Mockito.when(fileStorageService.getLastPathFolderName()).thenReturn("avatars");

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);
    Assert.assertEquals(200, response.getStatusCodeValue());
    Assert.assertEquals("AVATAR_1_avatar.png",((UploadFileResponseDTO)response.getBody()).getFileName());
    Assert.assertEquals("image/png", ((UploadFileResponseDTO)response.getBody()).getFileType());
  }

  @Test (expected = WrongContentTypeException.class)
  public void uploadAvatar_NotImageContent_throwsWronContentTypeException() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "text/plain", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    Mockito.when(fileStorageService.storeAvatar(file, player)).thenThrow(WrongContentTypeException.class);

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);
  }

  @Test (expected = FileStorageException.class)
  public void uploadAvatar_StorageNotAvailable_throwsFileStorageException() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "text/plain", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    Mockito.when(fileStorageService.storeAvatar(file, player)).thenThrow(FileStorageException.class);

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);
  }

}
