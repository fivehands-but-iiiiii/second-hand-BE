package com.team5.secondhand.chat.chatroom.repository.entity;

import com.team5.secondhand.chat.chatroom.domain.Chatroom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Entity
@Table(name = "chatroom_meta_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatroomMetaInfoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatroomId;
    private String lastMessage;
    private Instant updatedAt;
    @ElementCollection
    @MapKeyColumn(name = "member_id")
    @CollectionTable(name = "chatroom_participant_info", joinColumns = @JoinColumn(name = "chatroom_meta_info_id", referencedColumnName = "id"))
    private Map<Long, ParticipantInfoEntity> participants = new ConcurrentHashMap<>();

    @Builder
    protected ChatroomMetaInfoEntity(Long id, String chatroomId, String lastMessage, Instant updatedAt, Map<Long, ParticipantInfoEntity> participants) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.lastMessage = lastMessage;
        this.updatedAt = updatedAt;
        this.participants = participants;
    }

    public static ChatroomMetaInfoEntity fromDomain(Chatroom chatroom) {
        return ChatroomMetaInfoEntity.builder()
                .id(chatroom.getId())
                .chatroomId(chatroom.getChatroomId())
                .lastMessage(chatroom.getLastMessage())
                .updatedAt(Instant.parse(chatroom.getUpdateAt()))
                .participants(ParticipantInfoEntity.fromDomain(chatroom.getParticipants(), chatroom.getChatroomId()))
                .build();
    }

    public Chatroom toDomain() {
        return Chatroom.builder()
                .id(this.id)
                .chatroomId(this.chatroomId)
                .lastMessage(this.lastMessage)
                .updateAt(this.updatedAt.toString())
                .participants(ParticipantInfoEntity.toDomain(this.participants))
                .build();
    }

}
