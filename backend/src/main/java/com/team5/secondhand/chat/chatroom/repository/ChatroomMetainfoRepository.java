package com.team5.secondhand.chat.chatroom.repository;

import com.team5.secondhand.chat.chatroom.repository.entity.ChatroomMetaInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatroomMetainfoRepository extends JpaRepository<ChatroomMetaInfoEntity, Long> {

    Optional<ChatroomMetaInfoEntity> findByChatroomId(String chatroomId);
}
