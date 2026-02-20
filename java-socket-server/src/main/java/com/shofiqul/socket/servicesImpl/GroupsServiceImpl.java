package com.shofiqul.socket.servicesImpl;

import com.shofiqul.socket.dto.ApiResponse;
import com.shofiqul.socket.entity.GroupsEntity;
import com.shofiqul.socket.repositories.GroupsRepo;
import com.shofiqul.socket.services.GroupsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupsServiceImpl implements GroupsService {

    private final GroupsRepo groupsRepo;

    @Override
    public ApiResponse<GroupsEntity> getBroadcastGroup() {
        try {
            return ApiResponse.ok(getBroadcastGroupEntity());
        } catch (Exception e) {
            final String msg = "Something went wrong while fetching the broadcast group";
            log.error(msg);
            return ApiResponse.error(null, msg);
        }
    }

    public GroupsEntity getBroadcastGroupEntity() {
        return groupsRepo.findByGroupCode("BRDCST").orElse(null);
    }
}
