package com.team5.secondhand.application.chatroom.repository;

import com.team5.secondhand.application.chatroom.domian.Chatroom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByItemId(Long itemId);
    Optional<Chatroom> findByChatroomId(String chatroomId);
    Optional<Chatroom> findByBuyer_IdAndItem_Id(Long buyerId, Long itemId);
    Optional<Chatroom> findBySellerIdAndItemId(Long sellerId, Long itemId);
    Slice<Chatroom> findAllByBuyerIdOrSellerIdOrderByIdDesc(Pageable page, Long buyerId, Long sellerId);
    Slice<Chatroom> findAllByItemIdOrderByIdDesc(Pageable page, Long itemId);
}
