package com.team5.secondhand.chat.chatroom.repository.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "chatroom_metainfo")
public class ChatroomMetaInfoEntity {

    @Id
    private Long id;
}
