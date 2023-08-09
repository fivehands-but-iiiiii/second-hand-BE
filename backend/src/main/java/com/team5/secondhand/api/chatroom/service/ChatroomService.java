package com.team5.secondhand.api.chatroom.service;

import com.team5.secondhand.api.chatroom.domian.Chatroom;
import com.team5.secondhand.api.chatroom.dto.ChatroomInfo;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomList;
import com.team5.secondhand.api.chatroom.dto.response.ChatroomSummary;
import com.team5.secondhand.api.chatroom.exception.BuyerException;
import com.team5.secondhand.api.chatroom.exception.ExistChatRoomException;
import com.team5.secondhand.api.chatroom.exception.NotChatroomMemberException;
import com.team5.secondhand.api.chatroom.repository.ChatroomRepository;
import com.team5.secondhand.api.item.domain.Item;
import com.team5.secondhand.api.member.domain.Member;
import com.team5.secondhand.global.event.chatroom.ChatroomCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatroomService {
    private final ChatroomRepository chatRoomRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public String create(Item item, Member buyer) throws ExistChatRoomException, BuyerException {
        if (chatRoomRepository.findByBuyer_IdAndItem_Id(buyer.getId(), item.getId()).isPresent()) {
            throw new ExistChatRoomException("이미 존재하는 채팅방입니다.");
        }
        if (item.isSameSellerAndBuyer(buyer)) {
            throw new BuyerException("구매자는 판매자와 같을 수 없습니다.");
        }

        Chatroom chatRoom = Chatroom.create(item, buyer);
        Chatroom savedChatroom = chatRoomRepository.save(chatRoom);

        eventPublisher.publishEvent(new ChatroomCreatedEvent(ChatroomInfo.from(savedChatroom)));

        return savedChatroom.getChatroomId().toString();
    }

    @Transactional
    public Optional<Chatroom> findChatroom(long memberId, long itemId) {
        return chatRoomRepository.findByBuyer_IdAndItem_Id(memberId, itemId);
    }

    @Transactional
    public Chatroom findByChatroomId(String chatroomId) throws ExistChatRoomException {
        return chatRoomRepository.findByChatroomId(UUID.fromString(chatroomId)).orElseThrow(() -> new ExistChatRoomException("해당하는 채팅방이 없습니다."));
    }

    @Transactional
    public ChatroomList findChatroomListByMember(Pageable page, Member member) {
        Slice<Chatroom> chatroomSlice = chatRoomRepository.findAllByBuyerIdOrSellerIdOrderByIdDesc(page, member.getId(), member.getId());
        List<ChatroomSummary> chatroomSummaries = chatroomSlice.getContent().stream()
                .map(c -> ChatroomSummary.of(c, member))
                .collect(Collectors.toList());

        return ChatroomList.of(chatroomSummaries, page.getPageNumber(), chatroomSlice.hasNext(), chatroomSlice.hasPrevious());
    }

    @Transactional
    public ChatroomList findChatroomListByItem(Pageable page, Item item) {
        Slice<Chatroom> chatroomSlice = chatRoomRepository.findAllByItemIdOrderByIdDesc(page, item.getId());
        List<ChatroomSummary> chatroomSummaries = chatroomSlice.getContent().stream()
                .map(ChatroomSummary::of)
                .collect(Collectors.toList());

        return ChatroomList.of(chatroomSummaries, page.getPageNumber(), chatroomSlice.hasNext(), chatroomSlice.hasPrevious());
    }

    @Transactional
    public void exitChatroom(Chatroom chatroom, Member member) throws NotChatroomMemberException {
        chatroom.exitMember(member);
        chatRoomRepository.save(chatroom);
    }
}
