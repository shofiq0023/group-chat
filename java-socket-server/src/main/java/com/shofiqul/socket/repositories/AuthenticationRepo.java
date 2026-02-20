package com.shofiqul.socket.repositories;

import com.shofiqul.socket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthenticationRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

}
