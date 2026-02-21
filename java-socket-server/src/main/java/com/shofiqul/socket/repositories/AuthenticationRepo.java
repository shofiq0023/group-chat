package com.shofiqul.socket.repositories;

import com.shofiqul.socket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthenticationRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("FROM UserEntity where (username = :userHandle OR email = :userHandle) AND password = :password")
    Optional<UserEntity> findByUserHandleAndPassword(String userHandle, String password);

    @Query("FROM UserEntity WHERE username = :userHandle OR email = :userHandle")
    Optional<UserEntity> findByUserHandle(String userHandle);

    Optional<UserEntity> findByUsernameOrEmail(String userHandle, String password);

}
