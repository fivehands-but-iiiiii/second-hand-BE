package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface MongoChatBubbleRepository extends MongoRepository<ChatBubble, UUID> {

    Slice<ChatBubble> findAllByRoomId(String roomId, Pageable pageable);

    List<ChatBubble> findAllByRoomIdOrderByCreatedAtDesc(String roomId);

    ChatBubble findFirstByOrderByIdDesc();
}
