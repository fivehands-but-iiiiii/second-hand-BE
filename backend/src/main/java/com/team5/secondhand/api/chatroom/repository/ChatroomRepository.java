package com.team5.secondhand.api.chatroom.repository;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByItemId(Long itemId);
    Optional<Chatroom> findByChatroomId(UUID chatroomId);
    Optional<Chatroom> findByBuyer_IdAndItem_Id(Long buyerId, Long itemId);
    Slice<Chatroom> findAllByBuyerIdOrSellerIdOrderByIdDesc(Pageable page, Long buyerId, Long sellerId);
    Slice<Chatroom> findAllByItemIdOrderByIdDesc(Pageable page, Long itemId);
}
