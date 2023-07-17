package com.team5.secondhand.api.chatroom.repository;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {
    Optional<Chatroom> findByItemId(Long itemId);
    Optional<Chatroom> findByBuyer_IdAndItem_Id(Long buyerId, Long itemId);
    Optional<Chatroom> findByItemAndBuyer(Item item, Member buyer);
}
