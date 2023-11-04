package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import com.team5.secondhand.chat.bubble.repository.entity.ChatBubbleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataChatBubbleRepository extends JpaRepository<ChatBubbleEntity, Long> {
}