package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.UserEntity;
import com.greenfoxacademy.springwebapp.models.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.models.dtos.LoginStatusOkDTO;
import com.greenfoxacademy.springwebapp.models.dtos.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginExceptionServiceImp implements LoginExceptionService {

  private UserEntityService userEntityService;

  public LoginExceptionServiceImp(UserEntityService userEntityService) {
    this.userEntityService = userEntityService;
  }

  @Override
  public ResponseEntity<?> loginExceptions(UserDTO userDTO) {
    ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO();
    LoginStatusOkDTO loginStatusOkDTO = new LoginStatusOkDTO();
    String error = "error";
    if (userEntityService.countUsers() < 0) {
      if (userDTO.getUsername() == null) {
        errorMessageDTO.setStatus(error);
        errorMessageDTO.setMessage("Username is required.");
        return ResponseEntity.status(400).body(errorMessageDTO);
      } else if (userDTO.getPassword() == null) {
        errorMessageDTO.setStatus(error);
        errorMessageDTO.setMessage("Password is required.");
        return ResponseEntity.status(400).body(errorMessageDTO);
      } else if (userDTO.getUsername() == null && userDTO.getPassword() == null) {
        errorMessageDTO.setMessage(error);
        errorMessageDTO.setStatus("Username and Password are required.");
        return ResponseEntity.status(400).body(errorMessageDTO);
      } else if (userEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword()) == null) {
        errorMessageDTO.setStatus(error);
        errorMessageDTO.setMessage("Username or password is incorrect.");
        return ResponseEntity.status(401).body(errorMessageDTO);
      } else {
        UserEntity userEntity = userEntityService.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        loginStatusOkDTO.setStatus("ok");
        loginStatusOkDTO.setToken("token");
        return ResponseEntity.ok().body(loginStatusOkDTO);
      }
    }
    return null;
  }
}
