package com.greenfoxacademy.springwebapp.filestorage.services;

import com.greenfoxacademy.springwebapp.configuration.filestorageconfig.FileStorageProperties;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.FileStorageException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.ForbiddenActionException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.MyFileNotFoundException;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.WrongContentTypeException;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    FileStorageProperties fileStorageProperties;
    private final Path avatarStorageLocation;

    //injecting dependency to fileStorageProperties + using it immediately for defining fileStorageLocation
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;

        this.avatarStorageLocation = Paths.get(fileStorageProperties.getUploadAvatarDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.avatarStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
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
    public String getAvatarsFolderName() {
        String target = avatarStorageLocation.toString();
        int lastIndexVar1 = target.lastIndexOf("/");
        int lastIndexVar2 = target.lastIndexOf("\\");
        int lastIndex = Math.max(lastIndexVar1, lastIndexVar2);
        return target.substring(lastIndex + 1);
    }

    @Override
    public String getFileUrl(String folderName, String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(folderName + "/")
                .path(fileName)
                .toUriString();
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
    public String storeAvatar(MultipartFile file, PlayerEntity player)
            throws FileStorageException, WrongContentTypeException {
        // Normalize file name
        String fileName = StringUtils.cleanPath("AVATAR_" + player.getId() + "_"
                + player.getUsername() + "_" + file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters and is of type image
            checkCorrectnessOfFileNameAndFileType(fileName, file);
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.avatarStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            // Set avatar´s address into player object
            player.setAvatar(fileName);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
    }

    @Override
    public boolean userIsAllowedToAccessTheFile(String fileName, Authentication auth)
            throws ForbiddenActionException {

        String userId = ((CustomUserDetails) auth.getPrincipal()).getPlayer().getId().toString();
        int firstIndex = fileName.indexOf("_");
        int secondIndex = fileName.indexOf("_", firstIndex + 1);

        if (fileName.substring(firstIndex + 1, secondIndex).equals(userId)) return true;

        throw new ForbiddenActionException();
    }

}
