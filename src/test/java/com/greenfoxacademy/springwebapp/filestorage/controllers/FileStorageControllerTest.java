package com.greenfoxacademy.springwebapp.filestorage.controllers;

import static com.greenfoxacademy.springwebapp.factories.AuthFactory.createAuth;


import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.filestorage.models.dtos.FileResponseDTO;
import com.greenfoxacademy.springwebapp.filestorage.services.FileStorageService;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
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
  private PlayerService playerService;
  private Authentication authentication;

  @Before
  public void setup() {
    authentication = createAuth("test", 1L);
    fileStorageService = Mockito.mock(FileStorageService.class);
    playerService = Mockito.mock(PlayerService.class);
    fileStorageController = new FileStorageController(fileStorageService, playerService);
  }

  @Test
  public void uploadAvatar_isImageContentType_returnsCorrectDTO() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "image/png", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    PlayerEntity playerWithAvatar = player;
    playerWithAvatar.setAvatar("AVATAR_1_avatar.png");
    Mockito.when(fileStorageService.storeAvatar(file, player)).thenReturn("AVATAR_1_avatar.png");
    Mockito.when(fileStorageService.getAvatarsFolderName()).thenReturn("avatars");
    Mockito.when(playerService.setAvatar(player,file)).thenReturn(playerWithAvatar);

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);

    Assert.assertEquals(200, response.getStatusCodeValue());
    Assert.assertEquals("AVATAR_1_avatar.png",((FileResponseDTO)response.getBody()).getFileName());
    Assert.assertEquals("image/png", ((FileResponseDTO)response.getBody()).getFileType());
  }

  @Test (expected = WrongContentTypeException.class)
  public void uploadAvatar_isNotImageContentType_throwsWronContentTypeException() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "text/plain", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    Mockito.when(playerService.setAvatar(player,file)).thenThrow(WrongContentTypeException.class);

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);
  }

  @Test (expected = FileStorageException.class)
  public void uploadAvatar_StorageNotAvailable_throwsFileStorageException() throws Exception {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "text/plain", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    Mockito.when(playerService.setAvatar(player,file)).thenThrow(FileStorageException.class);

    ResponseEntity response = fileStorageController.uploadAvatar(file,authentication);
  }

}
