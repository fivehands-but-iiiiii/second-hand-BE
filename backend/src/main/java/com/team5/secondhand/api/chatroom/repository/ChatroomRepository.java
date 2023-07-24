package com.team5.secondhand.api.chatroom.repository;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByItemId(Long itemId);
    Optional<Chatroom> findByChatroomId(UUID chatroomId);
    Optional<Chatroom> findByBuyer_IdAndItem_Id(Long buyerId, Long itemId);
    List<Chatroom> findAllByBuyerIdOrSellerIdOrderByIdDesc(Long buyerId, Long sellerId);
    List<Chatroom> findAllByItemId(Long itemId);
}
