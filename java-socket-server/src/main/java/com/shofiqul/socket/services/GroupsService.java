package com.shofiqul.socket.services;

import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.entity.GroupsEntity;

public interface GroupsService {
    ApiResponse<GroupsEntity> getBroadcastGroup();
}
