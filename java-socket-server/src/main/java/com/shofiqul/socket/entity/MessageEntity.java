package com.shofiqul.socket.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity extends BaseEntity {

    @NotNull(message = "Sender cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @NotNull(message = "Group cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupsEntity group;

    @NotBlank(message = "Message content cannot be blank")
    @Size(max = 5000, message = "Message content must not exceed 5000 characters")
    @Column(nullable = false, length = 5000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replied_message_id")
    private MessageEntity repliedMessage;

    @OneToMany(mappedBy = "repliedMessage")
    @Builder.Default
    private Set<MessageEntity> replies = new HashSet<>();
}
