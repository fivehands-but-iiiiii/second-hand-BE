package com.team5.secondhand.chat.bubble.repository;

import com.team5.secondhand.chat.bubble.domain.ChatBubble;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBubbleRepository extends PagingAndSortingRepository<ChatBubble, String> {
    Slice<ChatBubble> findAllByRoomId(String roomId, Pageable pageable);
}
