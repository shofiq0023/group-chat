package com.shofiqul.socket.repositories;

import com.shofiqul.socket.entity.GroupsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupsRepo extends JpaRepository<GroupsEntity, Long> {
    Optional<GroupsEntity> findByGroupCode(String groupCode);
}
