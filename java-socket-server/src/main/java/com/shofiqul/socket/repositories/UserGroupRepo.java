package com.shofiqul.socket.repositories;

import com.shofiqul.socket.entity.UserGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepo extends JpaRepository<UserGroupEntity, Long> {
}
