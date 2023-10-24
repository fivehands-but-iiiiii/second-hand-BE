package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.repository.entity.ChatroomMetaInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomMetainfoRepository extends JpaRepository<ChatroomMetaInfoEntity, Long> {
}
