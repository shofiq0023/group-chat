package com.shofiqul.socket.entity;

import com.shofiqul.socket.enums.GroupPrivacy;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupsEntity extends BaseEntity {

    @NotBlank(message = "Group name cannot be blank")
    @Size(min = 3, max = 100, message = "Group name must be between 3 and 100 characters")
    @Column(unique = true, nullable = false)
    private String groupName;

    @NotBlank(message = "Group code cannot be blank")
    @Size(min = 4, max = 20, message = "Group code must be between 4 and 20 characters")
    @Column(unique = true, nullable = false)
    private String groupCode;

    private String groupImageLink;

    @NotBlank(message = "Group privacy cannot be blank")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GroupPrivacy groupPrivacy;

    @Size(min = 4, message = "Group password must be at least 4 characters long")
    private String groupPassword;

    @NotNull(message = "Member limitation flag cannot be null")
    @Column(nullable = false)
    private boolean hasMemberLimitations;

    @Min(value = 1, message = "Maximum group members must be at least 2")
    private int maxGroupMembers;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UserGroupEntity> groupUsers = new HashSet<>();
}
