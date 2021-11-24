package com.greenfoxacademy.springwebapp.player.controllers;

import com.greenfoxacademy.springwebapp.globalexceptionhandling.ErrorDTO;
import com.greenfoxacademy.springwebapp.globalexceptionhandling.InvalidTokenException;
import com.greenfoxacademy.springwebapp.kingdom.models.KingdomEntity;
import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.models.dtos.DeletedPlayerDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerListResponseDTO;
import com.greenfoxacademy.springwebapp.player.models.dtos.PlayerRegisterRequestDTO;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import com.greenfoxacademy.springwebapp.security.CustomUserDetails;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PlayerController {
    public static final String URI = "/register";
    public static final String URIVERIFY = URI + "/verify";
    private final PlayerService playerService;

    @GetMapping("/players/all-ids-by-verified")
    public ResponseEntity<List<Long>> allIdsByAccountVerified(@RequestParam boolean isVerified) {
        return ResponseEntity.ok(playerService.findAllByIsAccountVerified(isVerified));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/players/{deletedId}")
    public ResponseEntity<DeletedPlayerDTO> deletePlayer(@PathVariable Long deletedId) {
        return ResponseEntity.ok(playerService.deletePlayer(deletedId));
    }

    @GetMapping("/players/exists-by-id-and-verified")
    public ResponseEntity<Boolean> existsPlayerByIdAndIsVerified(@RequestParam long id, @RequestParam boolean isVerified) {
        return ResponseEntity.ok(playerService.existsPlayerByIdAndIsVerified(id, isVerified));
    }

    @GetMapping("/players")
    public ResponseEntity<PlayerListResponseDTO> getPlayersAroundMyKingdom(
            Authentication auth, @RequestParam(required = false) Integer distance) {
        KingdomEntity kingdom = ((CustomUserDetails) auth.getPrincipal()).getKingdom();
        PlayerListResponseDTO playerListResponseDTO = playerService.findPlayersAroundMe(kingdom, distance);
        return ResponseEntity.ok(playerListResponseDTO);
    }

    @PostMapping(URI)
    public ResponseEntity<?> registerUser(@RequestBody @Valid PlayerRegisterRequestDTO request)
            throws RuntimeException {
        PlayerEntity newRegistration = playerService.registerNewPlayer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.playerToResponseDTO(newRegistration));
    }

    @PatchMapping("/players/{id}/verify")
    public ResponseEntity<Boolean> verifyPlayer(@PathVariable @Min(1) long id, @RequestParam boolean isVerified) {
        playerService.setAccountVerified(id, isVerified);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Boolean.TRUE);
    }

    @GetMapping("/players/{id}/verify")
    public ResponseEntity<Boolean> verifyPlayer(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(playerService.selectIsVerified(id));
    }

    @GetMapping(URIVERIFY)
    @ResponseBody
    public ResponseEntity<ErrorDTO> verifyUser(@RequestParam(required = false) String token) {
        if (token.isEmpty()) {
            log.info(String.format("Verification token was incorrect. Used token: %s", token));
            return ResponseEntity.badRequest().body(new ErrorDTO("empty token"));
        }
        try {
            playerService.verifyUser(token);
        } catch (InvalidTokenException e) {
            log.info("Verification token is invalid. Used token: {}", token);
            return ResponseEntity.badRequest().body(new ErrorDTO("invalid token"));
        }
        log.info(String.format("Token: %s was verified", token));
        return ResponseEntity.ok().body(new ErrorDTO("ok", "verified"));
    }
}