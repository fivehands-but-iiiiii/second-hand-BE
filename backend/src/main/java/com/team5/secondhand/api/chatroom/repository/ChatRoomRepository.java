package com.team5.secondhand.api.chatroom.repository;

import com.team5.secondhand.api.chatroom.domian.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByItemId(Long itemId);

    Optional<ChatRoom> findByItemIdAndMemberId(Long itemId, Long memberId);
}
