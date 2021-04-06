package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.configuration.filestorageconfig.FileStorageProperties;
import com.greenfoxacademy.springwebapp.factories.AuthFactory;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;

public class FileStorageServiceTest {
  private FileStorageProperties fileStorageProperties;
  private PlayerService playerService;
  private FileStorageService fileStorageService;

  private Authentication authentication;

  @Before
  public void setUp() {
    fileStorageProperties = Mockito.mock(FileStorageProperties.class);
    playerService = Mockito.mock(PlayerService.class);
    Mockito.when(fileStorageProperties.getUploadAvatarDir()).thenReturn("src/test/resources/files/testAvatars");
    fileStorageService = new FileStorageServiceImpl(fileStorageProperties,playerService);

    authentication = AuthFactory.createAuth("Zdenek", 1L);
  }

  @Test
  public void storeAvatar_storesCorrectAvatarFileName_1() {
    MockMultipartFile file = new MockMultipartFile("someFile","avatar.png",
        "image/png", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    player.setId(1L);

    String fileName = fileStorageService.storeAvatar(file, player);

    Assert.assertEquals("AVATAR_1_Zdenek_avatar.png", fileName);
    Assert.assertEquals("AVATAR_1_Zdenek_avatar.png", player.getAvatar());
  }

  @Test
  public void storeAvatar_storesCorrectAvatarFileName_2() {
    MockMultipartFile file = new MockMultipartFile("someFile","another avatar.png",
        "image/png", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    player.setId(2L);

    String fileName = fileStorageService.storeAvatar(file, player);

    Assert.assertEquals("AVATAR_2_Zdenek_another avatar.png", fileName);
    Assert.assertEquals("AVATAR_2_Zdenek_another avatar.png", player.getAvatar());
  }

  @Test (expected = WrongContentTypeException.class)
  public void storeAvatar_throwsWrongContentTypeException() {
    MockMultipartFile file = new MockMultipartFile("someFile","another avatar.txt",
        "text/plain", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    player.setId(1L);

    String fileName = fileStorageService.storeAvatar(file, player);
  }

  @Test (expected = FileStorageException.class)
  public void storeAvatar_throwsFileStorageException() {
    MockMultipartFile file = new MockMultipartFile("someFile","another avatar..png",
        "image/png", "test data".getBytes());
    PlayerEntity player = ((CustomUserDetails) authentication.getPrincipal()).getPlayer();
    player.setId(1L);

    String fileName = fileStorageService.storeAvatar(file, player);
  }
}
