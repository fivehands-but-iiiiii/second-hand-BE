package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.entity.ChatBubbleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataChatBubbleRepository extends JpaRepository<ChatBubbleEntity, Long> {

    Slice<ChatBubbleEntity> findAllByChatroomId(String chatroomId, Pageable pageable);

    List<ChatBubbleEntity> findAllByChatroomIdOrderByIdDesc(String chatroomId);

    ChatBubbleEntity findFirstByOrderByIdDesc();
}
