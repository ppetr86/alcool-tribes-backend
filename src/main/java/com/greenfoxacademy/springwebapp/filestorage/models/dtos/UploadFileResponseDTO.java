package com.greenfoxacademy.springwebapp.filestorage.models.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileResponseDTO {
  private String fileName;
  private String fileDownloadUri;
  private String fileType;
  private long size;

  public UploadFileResponseDTO(String fileName, String fileDownloadUri, String fileType, long size) {
    this.fileName = fileName;
    this.fileDownloadUri = fileDownloadUri;
    this.fileType = fileType;
    this.size = size;
  }
}
