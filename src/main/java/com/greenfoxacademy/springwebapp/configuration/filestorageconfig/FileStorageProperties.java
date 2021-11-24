package com.greenfoxacademy.springwebapp.configuration.filestorageconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/* This class binds File Storage Properties defined in application.properties with methods in this class.
Prefix "file" binds properties starting with "file" prefix. If I add additional property, than new method using this
property shall be defined into this class.
 */

@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadAvatarDir;

    public String getUploadAvatarDir() {
        return uploadAvatarDir;
    }

    public void setUploadAvatarDir(String uploadAvatarDir) {
        this.uploadAvatarDir = uploadAvatarDir;
    }

}
