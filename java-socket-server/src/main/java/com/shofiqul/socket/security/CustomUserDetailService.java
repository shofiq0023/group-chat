package com.shofiqul.socket.security;

import com.shofiqul.socket.entity.UserEntity;
import com.shofiqul.socket.repositories.AuthenticationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AuthenticationRepo authRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = authRepo.findByUsername(username);

        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
