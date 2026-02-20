package com.shofiqul.socket.entity;

import com.shofiqul.socket.enums.GroupRole;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_groups", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "group_id"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupsEntity group;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_group_roles",
            joinColumns = @JoinColumn(name = "user_group_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @Builder.Default
    private Set<GroupRole> roles = new HashSet<>(Collections.singleton(GroupRole.MEMBER));

    @Column(nullable = false)
    private Timestamp joinedAt;
}
