package com.greenfoxacademy.springwebapp.filestorage.models.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileResponseDTO {
  private String fileName;
  private String fileDownloadUri;
  private String fileType;
  private long size;

  public FileResponseDTO(String fileName, String fileDownloadUri, String fileType, long size) {
    this.fileName = fileName;
    this.fileDownloadUri = fileDownloadUri;
    this.fileType = fileType;
    this.size = size;
  }

  public FileResponseDTO(MultipartFile file) {
    this.fileName = file.getOriginalFilename();
    this.fileDownloadUri = "/" + file.getOriginalFilename();
    this.fileType = file.getContentType();
    this.size = file.getSize();
  }

}
