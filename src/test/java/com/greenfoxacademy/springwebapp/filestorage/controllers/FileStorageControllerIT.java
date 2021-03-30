package com.greenfoxacademy.springwebapp.filestorage.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.factories.AuthFactory;
import java.io.File;
import java.nio.file.Files;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@Import(TestNoSecurityConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileStorageControllerIT {

  @Autowired
  private MockMvc mockMvc;

  private Authentication authentication;

  @Before
  public void setup() {
    authentication = AuthFactory.createAuthFullKingdom("Zdenek",1L);
  }

  @Test
  public void uploadAvatar_contentIsImage_returnsOkStatus() throws Exception {

    File file = new File("./src/test/resources/files/greenfox.png");
    MockMultipartFile upload = new MockMultipartFile("file", "greenfox.png",
        MediaType.IMAGE_PNG_VALUE,
        Files.readAllBytes(file.toPath()));

    mockMvc.perform(multipart(FileStorageController.AVATAR_URI)
        .file(upload)
        .principal(authentication))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.fileName", is("AVATAR_1_Zdenek_greenfox.png")))
        .andExpect(jsonPath("$.fileType", is("image/png")));
  }

  @Test
  public void uploadAvatar_contentIsNotImage_returns400Status() throws Exception {

    File file = new File("./src/test/resources/files/greenfox.txt");
    MockMultipartFile upload = new MockMultipartFile("file", "greenfox.txt",
        MediaType.TEXT_PLAIN_VALUE,
        Files.readAllBytes(file.toPath()));

    mockMvc.perform(multipart(FileStorageController.AVATAR_URI)
        .file(upload)
        .principal(authentication))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")));

  }
}
