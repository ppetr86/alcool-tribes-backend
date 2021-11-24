package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.player.models.PlayerEntity;
import com.greenfoxacademy.springwebapp.player.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PlayerService playerService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PlayerEntity player = playerService.findByUsername(username);
        if (player == null) {
            throw new UsernameNotFoundException("Player with username " + username + " was not found!");
        }
        return CustomUserDetails.fromPlayerToCustomUserDetails(player);
    }
}
