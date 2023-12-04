package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.repository.entity.BubbleEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatBubbleRepository extends JpaRepository<BubbleEntity, Long> {

    Slice<BubbleEntity> findAllByChatroomId(String chatroomId, Pageable pageable);

    BubbleEntity findFirstByOrderByIdDesc();
}
